package com.daniel.licenta.calendargenerator.algorithm.core;

import com.daniel.licenta.calendargenerator.algorithm.model.CalendarData;
import com.daniel.licenta.calendargenerator.algorithm.util.ArrayUtils;
import com.daniel.licenta.calendargenerator.algorithm.util.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.daniel.licenta.calendargenerator.algorithm.util.ArrayUtils.fillArrayWithRandomValuesInInteval;
import static com.daniel.licenta.calendargenerator.algorithm.util.CSOConstants.MAX_NUMBER_OF_STUDENT_CLASSES;
import static com.daniel.licenta.calendargenerator.algorithm.util.CSOConstants.REFINEMENT_STEPS;

@Component
public class OptimizerCSO {

    @Autowired
    private ConfigCSO configCSO;

    @Autowired
    private RandomGenerator randomGenerator;

    @Autowired
    private FitnessCalculator fitnessCalculator;

    private CalendarData calendarData;
    public int[][][] globalBestCat;

    public void setData(CalendarData calendarData, int[][][] globalBestCat) {
        this.calendarData = calendarData;
        this.globalBestCat = globalBestCat;
    }

    public int[][][] runOptimizationPhase(double TEPW, double ITDW, double ICDW, CalendarData calendarData, int[][][] globalBestCat) {
        this.calendarData = calendarData;
        this.globalBestCat = globalBestCat;

        System.out.print("\n\nOptimizing timetables ...\n");
        int[][][] helperArray = new int[MAX_NUMBER_OF_STUDENT_CLASSES][configCSO.HOURS_IN_WEEK][2];

        for (int start = 0; start < configCSO.HOURS_IN_WEEK; start = start + configCSO.HOURS_PER_DAY) {
            System.out.println("Optimization iteration : " + start);
            double partialFitnessOne = fitnessCalculator.calculatePartialFitness(start, start + configCSO.HOURS_PER_DAY, this.globalBestCat, TEPW);

            ArrayUtils.copyMatrices(start, start + configCSO.HOURS_PER_DAY, helperArray, this.globalBestCat, this.calendarData.totalNumberOfStudentClasses);

            for (int i = 0; i < REFINEMENT_STEPS; i++) {
                int[] timeStamps = fillArrayWithRandomValuesInInteval(new int[2], start, start + 6, 2, randomGenerator);

                performSwap(start, start + configCSO.HOURS_PER_DAY, helperArray, timeStamps[0], timeStamps[1], TEPW);
                double partialFitnessTwo = fitnessCalculator.calculatePartialFitness(start, start + configCSO.HOURS_PER_DAY, helperArray, TEPW);

                if (fitnessCalculator.checkHardConstraints(start, start + configCSO.HOURS_PER_DAY, helperArray) > 0.0) {
                    continue;
                }

                if (partialFitnessTwo <= partialFitnessOne) {
                    partialFitnessOne = partialFitnessTwo;
                    ArrayUtils.copyMatrices(start, start + configCSO.HOURS_PER_DAY, this.globalBestCat, helperArray, this.calendarData.totalNumberOfStudentClasses);

                    if (partialFitnessTwo == 0.0) {
                        break;
                    }
                }
            }

            double fullFitness = fitnessCalculator.calculateFitness(configCSO.HOURS_IN_WEEK, this.globalBestCat, TEPW, ITDW, ICDW);

            if (fullFitness == 0.0) {
                break;
            }
        }
        return this.globalBestCat;
    }

    private int checkValidity(int begin, int end, int[][][] a, int classPosition, int timeslot1, int timeslot2, double TEPW1) { // checks if there are hard constraint violations in a swap made during the optimization phase
        double ok3;
        double ok4;
        double f1;
        double f2;

        if ((a[classPosition][timeslot1][0] == -1 && a[classPosition][timeslot2][0] == -1)
                && // not sure of this
                (calendarData.rooms[a[classPosition][timeslot1][1]].capacity == calendarData.rooms[a[classPosition][timeslot2][1]].capacity)
                ) {
            return 1;
        }
        if ((a[classPosition][timeslot1][0] != -1 && calendarData.teachers[a[classPosition][timeslot1][0]].unavailableTimeslots[timeslot2] == 1)
                || (a[classPosition][timeslot1][0] != -1 && calendarData.teachers[a[classPosition][timeslot2][0]].unavailableTimeslots[timeslot1] == 1)) {
            return -1;
        }

        ok3 = fitnessCalculator.calculateParallelTeachingFitness(0, begin, end, a, 0);
        fitnessCalculator.swap(a, classPosition, timeslot1, timeslot2);

        ok4 = fitnessCalculator.calculateParallelTeachingFitness(0, begin, end, a, 0);

        if (ok4 > ok3) {
            fitnessCalculator.swap(a, classPosition, timeslot1, timeslot2);
            return -1;
        } else {
            fitnessCalculator.swap(a, classPosition, timeslot1, timeslot2);
        }

        ok3 = fitnessCalculator.calculateUnassignedStudentClassPeriodFitness(0, begin, end, a, 0);
        fitnessCalculator.swap(a, classPosition, timeslot1, timeslot2);
        ok4 = fitnessCalculator.calculateUnassignedStudentClassPeriodFitness(0, begin, end, a, 0);

        if (ok4 > ok3) {
            fitnessCalculator.swap(a, classPosition, timeslot1, timeslot2);
            return -1;

        } else {
            fitnessCalculator.swap(a, classPosition, timeslot1, timeslot2);
        }

        f1 = fitnessCalculator.calculateTeacherEmptyPeriodsFitness(0, begin, end, a, TEPW1, 0);

        fitnessCalculator.swap(a, classPosition, timeslot1, timeslot2);

        f2 = fitnessCalculator.calculateTeacherEmptyPeriodsFitness(0, begin, end, a, TEPW1, 0);
        if (f2 > f1) {
            fitnessCalculator.swap(a, classPosition, timeslot1, timeslot2);
            return -2;
        } else {
            fitnessCalculator.swap(a, classPosition, timeslot1, timeslot2);
        }
        return 1;
    }

    private boolean acceptSwap(int begin, int end, int[][][] a, int classPosition, int timeslot1, int timeslot2, double TEPW1) { // approves or rejects a swap during the optimization phase
        int ok = checkValidity(begin, end, a, classPosition, timeslot1, timeslot2, TEPW1);

        if (ok == 1) {
            return true;
        } else if (ok == -1) {
            return randomGenerator.nextNumber() <= 1.0 / 92.0;
        }

        return false;
    }

    private void performSwap(int begin, int end, int[][][] a, int timeslot1, int timeslot2, double TEPW1) { // performs a swap during the optimization phase
        for (int i = 0; i < calendarData.totalNumberOfStudentClasses; i++) {
            boolean ok = acceptSwap(begin, end, a, i, timeslot1, timeslot2, TEPW1);
            if (ok) {
                fitnessCalculator.swap(a, i, timeslot1, timeslot2);
            }
        }
    }

}
