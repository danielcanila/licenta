package com.daniel.licenta.calendargenerator.algorithm.core;

import com.daniel.licenta.calendargenerator.algorithm.model.CalendarData;
import com.daniel.licenta.calendargenerator.algorithm.util.ArrayUtils;
import com.daniel.licenta.calendargenerator.algorithm.util.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.daniel.licenta.calendargenerator.algorithm.core.ConfigCSO.HOURS_PER_DAY;
import static com.daniel.licenta.calendargenerator.algorithm.util.ArrayUtils.genUniqueRandoms;
import static com.daniel.licenta.calendargenerator.algorithm.util.CSOConstants.MAX_NUMBER_OF_STUDENT_CLASSES;
import static com.daniel.licenta.calendargenerator.algorithm.core.ConfigCSO.REFINEMENT_STEPS;

@Component
public class OptimizerCSO {

    @Autowired
    private ConfigCSO configCSO;

    @Autowired
    private RandomGenerator randomGenerator;

    @Autowired
    private FitnessCalculator fitnessCalculator;

    private CalendarData calendarData;

    public int[][][] runOptimizationPhase(double TEPW, double ITDW, double ICDW, CalendarData calendarData, int[][][] globalBestCat) {
        this.calendarData = calendarData;

        System.out.print("\n\nOptimizing timetables ...\n");
        int[][][] helperArray = new int[MAX_NUMBER_OF_STUDENT_CLASSES][configCSO.HOURS_IN_WEEK][2];

        for (int start = 0; start < configCSO.HOURS_IN_WEEK; start = start + (HOURS_PER_DAY)) {

            double partialFitnessOne = fitnessCalculator.calculatePartialFitness(start, start + HOURS_PER_DAY, globalBestCat, TEPW);

            ArrayUtils.copyMatrices(start, start + HOURS_PER_DAY, helperArray, globalBestCat, this.calendarData.studentCount);

            for (int i = 0; i < REFINEMENT_STEPS; i++) {
                List<Integer> timeStamps = genUniqueRandoms(start, start + 6, 2, randomGenerator);

                performSwap(start, start + HOURS_PER_DAY, helperArray, timeStamps.get(0), timeStamps.get(1), TEPW);
                double partialFitnessTwo = fitnessCalculator.calculatePartialFitness(start, start + HOURS_PER_DAY, helperArray, TEPW);

                if (fitnessCalculator.checkHardConstraints(start, start + HOURS_PER_DAY, helperArray) > 0.0) {
                    continue;
                }

                if (partialFitnessTwo <= partialFitnessOne) {
                    partialFitnessOne = partialFitnessTwo;
                    ArrayUtils.copyMatrices(start, start + HOURS_PER_DAY, globalBestCat, helperArray, this.calendarData.studentCount);

                    if (partialFitnessTwo == 0.0) {
                        break;
                    }
                }
            }

            double fullFitness = fitnessCalculator.calculateFitness(configCSO.HOURS_IN_WEEK, globalBestCat, TEPW, ITDW, ICDW);
            System.out.println("Optimization iteration cycle ended with fitness result of: " + fullFitness);
            if (fullFitness == 0.0) {
                break;
            }
        }
        return globalBestCat;
    }

    private int checkValidity(int begin, int end, int[][][] cat, int classPosition, int timeslot1, int timeslot2, double TEPW1) {
        if ((cat[classPosition][timeslot1][0] == -1 && cat[classPosition][timeslot2][0] == -1)
                && (calendarData.rooms[cat[classPosition][timeslot1][1]].capacity == calendarData.rooms[cat[classPosition][timeslot2][1]].capacity)) {
            return 1;
        }
        if ((cat[classPosition][timeslot1][0] != -1 && calendarData.teachers[cat[classPosition][timeslot1][0]].unavailableTimeslots[timeslot2] == 1)
                || (cat[classPosition][timeslot1][0] != -1 && calendarData.teachers[cat[classPosition][timeslot2][0]].unavailableTimeslots[timeslot1] == 1)) {
            return -1;
        }

        double ok3 = fitnessCalculator.calculateParallelTeachingFitness(begin, end, cat, 0);
        fitnessCalculator.swap(cat, classPosition, timeslot1, timeslot2);
        double ok4 = fitnessCalculator.calculateParallelTeachingFitness(begin, end, cat, 0);
        if (ok4 > ok3) {
            fitnessCalculator.swap(cat, classPosition, timeslot1, timeslot2);
            return -1;
        } else {
            fitnessCalculator.swap(cat, classPosition, timeslot1, timeslot2);
        }

        ok3 = fitnessCalculator.calculateUnassignedStudentClassPeriodFitness(begin, end, cat, 0);
        fitnessCalculator.swap(cat, classPosition, timeslot1, timeslot2);
        ok4 = fitnessCalculator.calculateUnassignedStudentClassPeriodFitness(begin, end, cat, 0);
        if (ok4 > ok3) {
            fitnessCalculator.swap(cat, classPosition, timeslot1, timeslot2);
            return -1;
        } else {
            fitnessCalculator.swap(cat, classPosition, timeslot1, timeslot2);
        }

        double f1 = fitnessCalculator.calculateTeacherEmptyPeriodsFitnessSoft(begin, end, cat, TEPW1, 0);
        fitnessCalculator.swap(cat, classPosition, timeslot1, timeslot2);
        double f2 = fitnessCalculator.calculateTeacherEmptyPeriodsFitnessSoft(begin, end, cat, TEPW1, 0);
        if (f2 > f1) {
            fitnessCalculator.swap(cat, classPosition, timeslot1, timeslot2);
            return -2;
        } else {
            fitnessCalculator.swap(cat, classPosition, timeslot1, timeslot2);
        }
        return 1;
    }

    private boolean acceptSwap(int begin, int end, int[][][] cat, int classPosition, int timeslot1, int timeslot2, double TEPW1) {
        int ok = checkValidity(begin, end, cat, classPosition, timeslot1, timeslot2, TEPW1);

        if (ok == 1) {
            return true;
        } else if (ok == -1) {
            return randomGenerator.nextNumber() <= 1.0 / 92.0;
        }

        return false;
    }

    private void performSwap(int begin, int end, int[][][] cat, int timeslot1, int timeslot2, double TEPW1) {
        for (int i = 0; i < calendarData.studentCount; i++) {
            boolean ok = acceptSwap(begin, end, cat, i, timeslot1, timeslot2, TEPW1);
            if (ok) {
                fitnessCalculator.swap(cat, i, timeslot1, timeslot2);
            }
        }
    }

}
