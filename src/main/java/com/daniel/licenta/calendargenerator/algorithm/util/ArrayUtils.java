package com.daniel.licenta.calendargenerator.algorithm.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ArrayUtils {

    public static void copyMatrices(int begin, int end, int[][][] to, int[][][] from, int dim) {
        for (int i = 0; i < dim; i++) {
            System.arraycopy(from[i], begin, to[i], begin, end - begin);
        }
    }

    public static int[][][] duplicateMatrice(int begin, int end, int[][][] source, int dim) {
        int[][][] destination = new int[source.length][source[0].length][source[0][0].length];
        for (int i = 0; i < dim; i++) {
            System.arraycopy(source[i], begin, destination[i], begin, end - begin);
        }

        return destination;
    }

    public static List<Integer> genUniqueRandoms(int lower, int upper, int number, RandomGenerator randomGenerator) {
        Set<Integer> uniqueRandoms = new HashSet<>();

        while (uniqueRandoms.size() != number) {
            uniqueRandoms.add(randomGenerator.nextInt(lower, upper - 1));
        }
        return new ArrayList<>(uniqueRandoms);
    }

}