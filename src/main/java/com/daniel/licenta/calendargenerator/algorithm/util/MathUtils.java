package com.daniel.licenta.calendargenerator.algorithm.util;

public class MathUtils {

    public static int roundNumber(double number) { // returns the rounded double number
        return (number >= 0) ? (int) (number + 0.5) : (int) (number - 0.5);
    }

}
