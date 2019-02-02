package com.daniel.licenta.calendargenerator.algorithm.core;

import com.daniel.licenta.calendargenerator.algorithm.model.CalendarData;
import com.daniel.licenta.calendargenerator.algorithm.util.ArrayUtils;
import com.daniel.licenta.calendargenerator.algorithm.util.MathUtils;
import com.daniel.licenta.calendargenerator.algorithm.util.RandomGenerator;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.daniel.licenta.calendargenerator.algorithm.core.ConfigCSO.*;
import static com.daniel.licenta.calendargenerator.algorithm.core.ConfigCSO.CDC;
import static com.daniel.licenta.calendargenerator.algorithm.core.ConfigCSO.HOURS_IN_WEEK;
import static com.daniel.licenta.calendargenerator.algorithm.core.ConfigCSO.SPC;
import static com.daniel.licenta.calendargenerator.algorithm.util.ArrayUtils.genUniqueRandoms;
import static com.daniel.licenta.calendargenerator.algorithm.util.CSOConstants.*;

@Component
public class MainCSO {

    public static final int c1 = 2;

    @Autowired
    private RandomGenerator randomGenerator;

    @Autowired
    private FitnessCalculator fitnessCalculator;

    @Autowired
    private LocalSearchOptimizerCSO optimizerCSO;

    private CalendarData data;
    private int[][][][] catData;
    private int[][][] globalBestCat;

    private Double globalBestFitness = MAX_FITNESS;


    public int[][][] runCsoCoreAlgorithm(double TEPW, double ITDW, CalendarData calendarData) {
        this.data = calendarData;
        initializeCats();


        for (int iterationStep = 0; iterationStep < ITERATIONS + 1; iterationStep++) {
            if (iterationStep == 0 || iterationStep % 10 == 0) {
                System.out.printf("Iterations %d, best fitness %f\n", iterationStep, globalBestFitness);
            }

            Stream.of(catData)
                    .filter(catData -> (randomGenerator.nextNumber() % 100) > MR)
                    .forEach(catData -> {
                        checkBestGlobal(TEPW, ITDW, calendarData, catData);
                        catSeek(catData, TEPW, ITDW);

                    });
            Stream.of(catData)
                    .filter(catData -> (randomGenerator.nextNumber() % 100) <= MR)
                    .forEach(catData -> {
                        checkBestGlobal(TEPW, ITDW, calendarData, catData);
                        catTrace(catData);
                    });

        }
        return globalBestCat;
    }

    private void checkBestGlobal(double TEPW, double ITDW, CalendarData calendarData, int[][][] catData) {
        double fitness = fitnessCalculator.calculateFitness(HOURS_IN_WEEK, catData, TEPW, ITDW);

        if (fitness <= globalBestFitness) {
            globalBestFitness = fitness;

            for (int k = 0; k < calendarData.studentCount; k++) {
                System.arraycopy(catData[k], 0, globalBestCat[k], 0, HOURS_IN_WEEK);
            }
        }
    }

    private void initializeCats() {
        catData = new int[CATS][MAX_NUMBER_OF_STUDENT_CLASSES][HOURS_IN_WEEK][3];
        globalBestCat = new int[MAX_NUMBER_OF_STUDENT_CLASSES][HOURS_IN_WEEK][3];


        for (int p = 0; p < CATS; p++) {
            for (int classPosition = data.studentCount - 1; classPosition >= 0; classPosition--) {
                for (int timeslot = 0; timeslot < HOURS_IN_WEEK; timeslot++) {
                    catData[p][classPosition][timeslot][0] = -1;
                    catData[p][classPosition][timeslot][1] = -2;
                    catData[p][classPosition][timeslot][2] = -3;
                }
                int requiredNumberOfTeachingHours = 0;
                for (int teacherPosition = 0; teacherPosition < data.studentGroups[classPosition].numberOfTeachers; teacherPosition++) {
                    requiredNumberOfTeachingHours += data.studentGroups[classPosition].teachersOfClassAndHours[teacherPosition][1];
                }
                if (requiredNumberOfTeachingHours > HOURS_IN_WEEK) {
                    throw new RuntimeException("Cannot calculate because group with position " + classPosition + " requires the following amount of hours: " + requiredNumberOfTeachingHours);
                }
                for (int teacherPosition = 0; teacherPosition < data.studentGroups[classPosition].numberOfTeachers; teacherPosition++) {
                    int stop = data.studentGroups[classPosition].teachersOfClassAndHours[teacherPosition][1];
                    while (stop > 0) {
                        int timeslot = randomGenerator.nextInt(0, HOURS_IN_WEEK - 1);


                        if (catData[p][classPosition][timeslot][0] == -1) {

                            catData[p][classPosition][timeslot][0] = data.studentGroups[classPosition].teachersOfClassAndHours[teacherPosition][0];
                            catData[p][classPosition][timeslot][1] = 0;
                            catData[p][classPosition][timeslot][2] = 0;
                            stop--;
                        }
                    }
                }
            }
        }
    }

    private void catSeek(int[][][] catData, double TEPW, double ITDW) {
        List<Pair<Integer, Double>> fitnessRecords = new ArrayList<>();
        double bestFitness = fitnessCalculator.calculateFitness(HOURS_IN_WEEK, catData, TEPW, ITDW);

        int[][][][] catDataCopy = new int[SEEKING_MEMORY_POOL][MAX_NUMBER_OF_STUDENT_CLASSES][HOURS_IN_WEEK][2];
        for (int cp = 0; cp < SEEKING_MEMORY_POOL; cp++) {
            ArrayUtils.copyMatrices(0, HOURS_IN_WEEK, catDataCopy[cp], catData, data.studentCount);
        }

        int timeslotsToChange = (int) Math.floor((CDC / 100.0) * HOURS_IN_WEEK);

        int swapsToMake = (int) Math.floor((SEEKING_RANGE_DIMENSION / 100.0) * data.studentCount * HOURS_IN_WEEK);

        for (int cp = 0; cp < SEEKING_MEMORY_POOL; cp++) {
            int[][][] temporaryCatToModify = ArrayUtils.duplicateMatrice(0, HOURS_IN_WEEK, catDataCopy[cp], data.studentCount);

            if (!SPC || (SPC && (cp != (SEEKING_MEMORY_POOL - 1)))) {

                genUniqueRandoms(0, HOURS_IN_WEEK - 1, timeslotsToChange, randomGenerator)
                        .forEach(randomTimeslot -> {
                            insertColumn(globalBestCat, temporaryCatToModify, randomTimeslot, TEPW, ITDW);
                        });

                int[][][] storedCopy = ArrayUtils.duplicateMatrice(0, HOURS_IN_WEEK, temporaryCatToModify, data.studentCount);

                for (int i = 0; i < swapsToMake; i++) {
                    int randomStudentClass = randomGenerator.nextInt(0, data.studentCount);
                    int randomTimeIntervalOne = randomGenerator.nextInt(0, HOURS_IN_WEEK - 1);
                    int randomTimeIntervalTwo = randomGenerator.nextInt(0, HOURS_IN_WEEK - 1);

                    if (singleSwap(temporaryCatToModify, randomTimeIntervalOne, randomTimeIntervalTwo, randomStudentClass)) {
                        double fitness = fitnessCalculator.calculateFitness(HOURS_IN_WEEK, temporaryCatToModify, TEPW, ITDW);
                        if (fitness <= bestFitness) {
                            bestFitness = fitness;
                            fitnessRecords.add(new Pair<>(cp, bestFitness));
                            // keep the new cat because it better
                            ArrayUtils.copyMatrices(0, HOURS_IN_WEEK, catDataCopy[cp], temporaryCatToModify, data.studentCount);
                        }
                        // revert cat one to original one which had better fitness
                        ArrayUtils.copyMatrices(0, HOURS_IN_WEEK, temporaryCatToModify, storedCopy, data.studentCount);
                    }
                }
            } else {
                fitnessRecords.add(new Pair<>(cp, fitnessCalculator.calculateFitness(HOURS_IN_WEEK, catDataCopy[cp], TEPW, ITDW)));
            }
        }

        Pair<Integer, Double> maxFitness = fitnessRecords.get(0);
        Pair<Integer, Double> minFitness = fitnessRecords.get(0);
        for (Pair<Integer, Double> record : fitnessRecords) {
            if (record.getValue() > maxFitness.getValue()) {
                maxFitness = record;
            }
            if (record.getValue() < minFitness.getValue()) {
                minFitness = record;
            }
        }

        if (maxFitness.getValue().equals(minFitness.getValue())) {
            // all fitness values are the same so we take a random one
            int selectedCopy = randomGenerator.nextInt(0, SEEKING_MEMORY_POOL - 1);
            ArrayUtils.copyMatrices(0, HOURS_IN_WEEK, catData, catDataCopy[selectedCopy], data.teacherCount);
        } else {
            // we take the best option
            ArrayUtils.copyMatrices(0, HOURS_IN_WEEK, catData, catDataCopy[minFitness.getKey()], data.teacherCount);
        }
    }

    private void catTrace(int[][][] catInTrace) {
        double bestFitness = fitnessCalculator.checkHardConstraints(0, HOURS_IN_WEEK, catInTrace);
        double globalBestFitness = fitnessCalculator.checkHardConstraints(0, HOURS_IN_WEEK, globalBestCat);

        double distance = c1 * (bestFitness - globalBestFitness);
        int swapsToMake = MathUtils.roundNumber((randomGenerator.nextDouble() * distance));

        for (int i = 0; i < swapsToMake; i++) {
            singleSwap(catInTrace,
                    randomGenerator.nextInt(0, HOURS_IN_WEEK - 1),
                    randomGenerator.nextInt(0, HOURS_IN_WEEK - 1),
                    randomGenerator.nextInt(0, data.studentCount));
        }
    }

    // TODO : refactor this better
    // replaces all the lessons in a timeslot of a cat with the ones in the same timeslot of another cat
    private void insertColumn(int[][][] source, int[][][] destination, int timeslot, double TEPW, double ITDW) {
        int[] aux = new int[HOURS_IN_WEEK];
        double[][] storePositionsAndFitness = new double[2][HOURS_IN_WEEK];

        for (int i = 0; i < data.studentCount; i++) {
            if ((timeslot == HOURS_PER_DAY * 1 - 1
                    || timeslot == HOURS_PER_DAY * 2 - 1
                    || timeslot == HOURS_PER_DAY * 3 - 1
                    || timeslot == HOURS_PER_DAY * 4 - 1
                    || timeslot == HOURS_PER_DAY * 5 - 1)
                    && destination[i][timeslot][0] == -1) {
                continue;
            }

            if (destination[i][timeslot] == source[i][timeslot]) {
                continue;
            }

            int jj = 0;

            for (int j = 0; j < HOURS_IN_WEEK; j++) {
                if (destination[i][j] == source[i][timeslot] && j != timeslot) {
                    aux[jj] = j;
                    jj++;
                }
            }
            int skip = 0;
            for (int z = 0; z < jj; z++) {
                if (aux[z] == timeslot) {
                    skip = 1;
                    continue;
                }

                int[] temp = destination[i][aux[z]];
                destination[i][aux[z]] = destination[i][timeslot];
                destination[i][timeslot] = temp;

                double ff = fitnessCalculator.calculateFitness(HOURS_IN_WEEK, destination, TEPW, ITDW);

                if (skip == 0) {
                    storePositionsAndFitness[0][z] = aux[z];
                    storePositionsAndFitness[1][z] = ff;
                } else {
                    storePositionsAndFitness[0][z - 1] = aux[z];
                    storePositionsAndFitness[1][z - 1] = ff;
                }

                temp = destination[i][aux[z]];
                destination[i][aux[z]] = destination[i][timeslot];
                destination[i][timeslot] = temp;
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
            destination[i][aux[index]] = destination[i][timeslot];
            destination[i][timeslot] = temp;
        }
    }

    private boolean singleSwap(int[][][] cat, int timeslotOne, int timeslotTwo, int studentClassIndex) {
        //same timeslot
        if (timeslotOne == timeslotTwo) {
            return false;
        }
        //same teacher
        if (cat[studentClassIndex][timeslotOne][0] == cat[studentClassIndex][timeslotTwo][0]) {
            return false;
        }
        //no teacher assigned
        if (cat[studentClassIndex][timeslotOne][0] == -1 || cat[studentClassIndex][timeslotTwo][0] == -1) {
            return false;
        }

        //this is to make sure that the swap does not assign the same teacher twice at the same hour
        for (int i = 0; i < data.studentCount; i++) {
            if (cat[i][timeslotOne][0] == cat[studentClassIndex][timeslotTwo][0]) {
                return false;
            }
            if (cat[i][timeslotTwo][0] == cat[studentClassIndex][timeslotOne][0]) {
                return false;
            }
        }

        fitnessCalculator.swap(cat, studentClassIndex, timeslotOne, timeslotTwo);

        return true;
    }
}
