package com.daniel.licenta.calendargenerator.algorithm.core;

import com.daniel.licenta.calendargenerator.algorithm.model.CalendarData;
import com.daniel.licenta.calendargenerator.algorithm.model.RoomRecord;
import com.daniel.licenta.calendargenerator.algorithm.util.ArrayUtils;
import com.daniel.licenta.calendargenerator.algorithm.util.MathUtils;
import com.daniel.licenta.calendargenerator.algorithm.util.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.daniel.licenta.calendargenerator.algorithm.util.ArrayUtils.fillArrayWithRandomValuesInInteval;
import static com.daniel.licenta.calendargenerator.algorithm.util.CSOConstants.*;

@Component
public class MainCSO {

    @Autowired
    private ConfigCSO configCSO;

    @Autowired
    private RandomGenerator randomGenerator;

    @Autowired
    private FitnessCalculator fitnessCalculator;

    private CalendarData calendarData;
    private int[][][][] catData;
    private int[][][] globalBestCat;

    public int[][][] runCsoCoreAlgorithm(double TEPW, double ITDW, double ICDW, CalendarData calendarData) {
        this.calendarData = calendarData;
        initializeCats();

        double globalBestFitness = MAX_FITNESS;

        for (int iterator = 0; iterator < ITERATIONS + 1; iterator++) {
            if (iterator == 0 || iterator % 100 == 0) {
                System.out.printf("Iterations %d, best fitness %f\n", iterator, globalBestFitness);
            }

            for (int p = 0; p < CATS; p++) {
                double fitness = fitnessCalculator.calculateFitness(configCSO.HOURS_IN_WEEK, catData[p], TEPW, ITDW, ICDW);

                if (fitness <= globalBestFitness) {
                    globalBestFitness = fitness;

                    for (int k = 0; k < calendarData.totalNumberOfStudentClasses; k++) {
                        System.arraycopy(catData[p][k], 0, globalBestCat[k], 0, configCSO.HOURS_IN_WEEK);
                    }
                }
                if ((randomGenerator.nextNumber() % 100) > MR) {
                    catSeek(catData[p], TEPW, ITDW, ICDW);
                } else {
                    catTrace(catData[p]);
                }
            }
        }
        return globalBestCat;
    }

    private void initializeCats() {
        catData = new int[CATS][MAX_NUMBER_OF_STUDENT_CLASSES][configCSO.HOURS_IN_WEEK][3];
        globalBestCat = new int[MAX_NUMBER_OF_STUDENT_CLASSES][configCSO.HOURS_IN_WEEK][3];


        for (int p = 0; p < CATS; p++) {
            int[][] roomData = new int[calendarData.rooms.length][configCSO.HOURS_IN_WEEK];
            for (int i = 0; i < calendarData.rooms.length; i++) {
                for (int j = 0; j < configCSO.HOURS_IN_WEEK; j++) {
                    roomData[i][j] = -1;
                }
            }
            for (int classPosition = 0; classPosition < calendarData.totalNumberOfStudentClasses; classPosition++) {
                for (int timeslot = 0; timeslot < configCSO.HOURS_IN_WEEK; timeslot++) {
                    catData[p][classPosition][timeslot][0] = -1;
                    catData[p][classPosition][timeslot][1] = -1;
                    catData[p][classPosition][timeslot][2] = -1;
                }

                int requiredNumberOfTeachingHours = 0;
                for (int teacherPosition = 0; teacherPosition < calendarData.studentGroups[classPosition].numberOfTeachers; teacherPosition++) {
                    requiredNumberOfTeachingHours += calendarData.studentGroups[classPosition].teachersOfClassAndHours[teacherPosition][1];
                }
                if (requiredNumberOfTeachingHours > configCSO.HOURS_IN_WEEK) {
                    throw new RuntimeException("Cannot calculate because group with position " + classPosition + " requires the following amount of hours: " + requiredNumberOfTeachingHours);
                }

                for (int teacherPosition = 0; teacherPosition < calendarData.studentGroups[classPosition].numberOfTeachers; teacherPosition++) {
                    int stop = calendarData.studentGroups[classPosition].teachersOfClassAndHours[teacherPosition][1];
                    while (stop > 0) {
                        int timeslot = randomGenerator.nextInt(0, configCSO.HOURS_IN_WEEK - 1);


                        int capacity = calendarData.studentGroups[classPosition].numberOfStudents;
                        List<RoomRecord> roomsThatFitCapacity = calendarData.getAllRoomsByCapacity(capacity);

                        for (RoomRecord roomRecord : roomsThatFitCapacity) {
                            if (roomData[roomRecord.roomIndex][timeslot] == -1) {
                                if (catData[p][classPosition][timeslot][0] == -1 && catData[p][classPosition][timeslot][1] == -1) {

                                    catData[p][classPosition][timeslot][0] = calendarData.studentGroups[classPosition].teachersOfClassAndHours[teacherPosition][0];
                                    catData[p][classPosition][timeslot][1] = roomRecord.roomIndex;
                                    catData[p][classPosition][timeslot][2] = capacity;
                                    roomData[roomRecord.roomIndex][timeslot] = 1;
                                    stop--;
                                    break;
                                }
                            }
                        }


                    }
                }
            }
        }
    }

    private void catSeek(int[][][] catData, double TEPW, double ITDW, double ICDW) {
        int consider;
        double[] fs = new double[SEEKING_MEMORY_POOL];
        double[] cfs = new double[SEEKING_MEMORY_POOL];
        int[] sl = new int[calendarData.totalNumberOfStudentClasses * configCSO.HOURS_IN_WEEK];
        int[][][][] catCopy = new int[SEEKING_MEMORY_POOL][MAX_NUMBER_OF_STUDENT_CLASSES][configCSO.HOURS_IN_WEEK][2];
        int[][][] temporaryCat1 = new int[MAX_NUMBER_OF_STUDENT_CLASSES][configCSO.HOURS_IN_WEEK][2];
        int[][][] temporaryCat2 = new int[MAX_NUMBER_OF_STUDENT_CLASSES][configCSO.HOURS_IN_WEEK][2];

        double bestFitness = fitnessCalculator.calculateFitness(configCSO.HOURS_IN_WEEK, catData, TEPW, ITDW, ICDW);

        if (SPC == 1) {
            consider = 1;
        } else {
            consider = 0;
        }

        for (int cp = 0; cp < SEEKING_MEMORY_POOL; cp++) {
            ArrayUtils.copyMatrices(0, configCSO.HOURS_IN_WEEK, catCopy[cp], catData, calendarData.totalNumberOfStudentClasses);
        }

        int timeslotsToChange = (int) Math.floor((CDC / 100.0) * configCSO.HOURS_IN_WEEK);
        if (timeslotsToChange == 0) {
            timeslotsToChange = 1;
        }

        int swapsToMake = (int) Math.floor((SEEKING_RANGE_DIMENSION / 100.0) * calendarData.totalNumberOfStudentClasses * configCSO.HOURS_IN_WEEK);

        for (int cp = 0; cp < SEEKING_MEMORY_POOL; cp++) {
            ArrayUtils.copyMatrices(0, configCSO.HOURS_IN_WEEK, temporaryCat1, catCopy[cp], calendarData.totalNumberOfStudentClasses);

            if ((consider == 0) || ((consider == 1) && (cp != SEEKING_MEMORY_POOL - 1))) {
                int[] hd = fillArrayWithRandomValuesInInteval(new int[configCSO.HOURS_IN_WEEK], 0, configCSO.HOURS_IN_WEEK - 1, timeslotsToChange, randomGenerator);

                for (int aa = 0; aa < timeslotsToChange; aa++) {
                    insertColumn(globalBestCat, temporaryCat1, hd[aa], TEPW, ITDW, ICDW);
                }

                ArrayUtils.copyMatrices(0, configCSO.HOURS_IN_WEEK, temporaryCat2, temporaryCat1, calendarData.totalNumberOfStudentClasses);
                fillArrayWithRandomValuesInInteval(sl, 0, (calendarData.totalNumberOfStudentClasses * configCSO.HOURS_IN_WEEK) - 1, swapsToMake, randomGenerator);

                for (int bb = 0; bb < swapsToMake; bb++) {
                    int cn = (int) Math.floor(sl[bb] / configCSO.HOURS_IN_WEEK);
                    int tt1 = sl[bb] % configCSO.HOURS_IN_WEEK;
                    int tt2 = randomGenerator.nextInt(0, configCSO.HOURS_IN_WEEK - 1);

                    if (singleSwap(temporaryCat1, tt1, tt2, cn) != -1) {
                        double fitness = fitnessCalculator.calculateFitness(configCSO.HOURS_IN_WEEK, temporaryCat1, TEPW, ITDW, ICDW);
                        if (fitness <= bestFitness) {
                            bestFitness = fitness;
                            fs[cp] = fitness;
                            cfs[cp] = fitness;
                            ArrayUtils.copyMatrices(0, configCSO.HOURS_IN_WEEK, catCopy[cp], temporaryCat1, calendarData.totalNumberOfStudentClasses);
                        }
                        ArrayUtils.copyMatrices(0, configCSO.HOURS_IN_WEEK, temporaryCat1, temporaryCat2, calendarData.totalNumberOfStudentClasses);
                    }
                }
            } else {
                fs[cp] = fitnessCalculator.calculateFitness(configCSO.HOURS_IN_WEEK, catCopy[cp], TEPW, ITDW, ICDW);
                cfs[cp] = fs[cp];
            }
        }

        for (int i = 0; i < SEEKING_MEMORY_POOL; i++) {
            for (int j = 1; j < SEEKING_MEMORY_POOL; j++) {
                if (cfs[j] < cfs[j - 1]) {
                    double ll = cfs[j - 1];
                    cfs[j - 1] = cfs[j];
                    cfs[j] = ll;
                }
            }
        }

        double maxFitness = cfs[SEEKING_MEMORY_POOL - 1];
        double minFitness = cfs[0];
        int all_equal = 0;

        if (maxFitness == minFitness) {
            all_equal = 1;
        }

        if (all_equal == 1) {
            int selectedCopy = randomGenerator.nextInt(0, SEEKING_MEMORY_POOL - 1);
            ArrayUtils.copyMatrices(0, configCSO.HOURS_IN_WEEK, catData, catCopy[selectedCopy], calendarData.totalNumberOfTeachers);
        } else {
            double[] selProb = new double[SEEKING_MEMORY_POOL];
            for (int i = 0; i < SEEKING_MEMORY_POOL; i++) {
                selProb[i] = Math.abs(fs[i] - maxFitness) / (maxFitness - minFitness);
            }

            double newRandom = randomGenerator.nextDouble();

            for (int i = 0; i < SEEKING_MEMORY_POOL; i++) {
                if (newRandom <= selProb[i]) {
                    ArrayUtils.copyMatrices(0, configCSO.HOURS_IN_WEEK, catData, catCopy[i], calendarData.totalNumberOfTeachers);
                }
            }
        }
    }

    private void catTrace(int[][][] x) {
        int[] sl = new int[calendarData.totalNumberOfStudentClasses * configCSO.HOURS_IN_WEEK];

        int similarity = 0;
        for (int k = 0; k < calendarData.totalNumberOfStudentClasses; k++) {
            for (int j = 0; j < configCSO.HOURS_IN_WEEK; j++) {
                if (x[k][j] == globalBestCat[k][j]) {
                    similarity++;
                }
            }
        }

        int distance = (calendarData.totalNumberOfStudentClasses * configCSO.HOURS_IN_WEEK) - similarity;
        int cells_to_swap = MathUtils.roundNumber((randomGenerator.nextDouble() * (double) distance));

        fillArrayWithRandomValuesInInteval(sl, 0, calendarData.totalNumberOfStudentClasses * configCSO.HOURS_IN_WEEK, cells_to_swap, randomGenerator);

        for (int k = 0; k < cells_to_swap; k++) {
            int cn = (int) Math.floor(sl[k] / configCSO.HOURS_IN_WEEK);
            int tt1 = sl[k] % configCSO.HOURS_IN_WEEK;
            int tt2 = randomGenerator.nextInt(0, configCSO.HOURS_IN_WEEK - 1);

            singleSwap(x, tt1, tt2, cn);
        }
    }

    // TODO : analyse this better
    // replaces all the lessons in a timeslot of a cat with the ones in the same timeslot of another cat
    private void insertColumn(int[][][] source, int[][][] destination, int column, double TEPW, double ITDW, double ICDW) {
        int[] aux = new int[configCSO.HOURS_IN_WEEK];
        double[][] storePositionsAndFitness = new double[2][configCSO.HOURS_IN_WEEK];

        for (int i = 0; i < calendarData.totalNumberOfStudentClasses; i++) {
            if ((column == configCSO.HOURS_PER_DAY - 1
                    || column == 2 * configCSO.HOURS_PER_DAY - 1
                    || column == 3 * configCSO.HOURS_PER_DAY - 1
                    || column == 4 * configCSO.HOURS_PER_DAY - 1
                    || column == 5 * configCSO.HOURS_PER_DAY - 1) && destination[i][column][0] == -1) {
                continue;
            }

            if (destination[i][column] == source[i][column]) {
                continue;
            }

            int jj = 0;

            for (int j = 0; j < configCSO.HOURS_IN_WEEK; j++) {
                if (destination[i][j] == source[i][column] && j != column) {
                    aux[jj] = j;
                    jj++;
                }
            }
            int skip = 0;
            for (int z = 0; z < jj; z++) {
                if (aux[z] == column) {
                    skip = 1;
                    continue;
                }

                int[] temp = destination[i][aux[z]];
                destination[i][aux[z]] = destination[i][column];
                destination[i][column] = temp;

                double ff = fitnessCalculator.calculateFitness(configCSO.HOURS_IN_WEEK, destination, TEPW, ITDW, ICDW);

                if (skip == 0) {
                    storePositionsAndFitness[0][z] = aux[z];
                    storePositionsAndFitness[1][z] = ff;
                } else {
                    storePositionsAndFitness[0][z - 1] = aux[z];
                    storePositionsAndFitness[1][z - 1] = ff;
                }

                temp = destination[i][aux[z]];
                destination[i][aux[z]] = destination[i][column];
                destination[i][column] = temp;
            }

            double smallerFitness = MAX_FITNESS;
            int index = 0;
            for (int z = 0; z < jj; z++) {
                if (storePositionsAndFitness[1][z] < smallerFitness) {
                    smallerFitness = storePositionsAndFitness[1][z];
                    index = z;
                }
            }
            int[] temp = destination[i][aux[index]];
            destination[i][aux[index]] = destination[i][column];
            destination[i][column] = temp;
        }
    }

    private int singleSwap(int[][][] cat, int timeslot1, int timeslot2, int classNum) {
        if (timeslot1 == timeslot2) {
            return -1;
        }
        if (cat[classNum][timeslot1][0] == cat[classNum][timeslot2][0]) {
            return -1;
        }
        if (cat[classNum][timeslot1][0] == -1 || cat[classNum][timeslot2][0] == -1) {
            return -1;
        }

        if (cat[classNum][timeslot1][2] != cat[classNum][timeslot2][2]) {
            return -1;
        }

        for (int i = 0; i < calendarData.totalNumberOfStudentClasses; i++) {
            if (cat[i][timeslot1][0] == cat[classNum][timeslot2][0]) {
                return -1;
            }
            if (cat[i][timeslot2][0] == cat[classNum][timeslot1][0]) {
                return -1;
            }
        }


        fitnessCalculator.swap(cat, classNum, timeslot1, timeslot2);

        return 1;
    }
}
