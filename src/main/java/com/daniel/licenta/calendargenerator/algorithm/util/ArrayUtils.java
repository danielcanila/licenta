package com.daniel.licenta.calendargenerator.algorithm.util;

import com.daniel.licenta.calendargenerator.algorithm.model.StudentGroup;

public class ArrayUtils {


    public static StudentGroup[] initializeWithDefaultclass_recordInstances(int length) {
        StudentGroup[] array = new StudentGroup[length];
        for (int i = 0; i < length; i++) {
            array[i] = new StudentGroup();
        }
        return array;
    }

    public static void copyMatrices(int begin, int end, int[][][] destination, int[][][] source, int dim) {
        for (int i = 0; i < dim; i++) {
            System.arraycopy(source[i], begin, destination[i], begin, end - begin);
        }
    }

    public static int[] fillArrayWithRandomValuesInInteval(int[] array, int lower, int upper, int number, RandomGenerator randomGenerator) {
        for (int i = 0; i < number; i++) {
            if (i == 0) {
                array[i] = randomGenerator.nextInt(lower, upper - 1);
            } else {
                array[i] = randomGenerator.nextInt(lower, upper - 1);

                for (int j = 0; j < i; j++) {
                    if (array[i] == array[j]) {
                        j = 0;
                        array[i] = randomGenerator.nextInt(lower, upper - 1);

                        while (array[i] == array[j]) {
                            array[i] = randomGenerator.nextInt(lower, upper - 1);
                        }
                    }
                }
            }
        }
        return array;
    }

}