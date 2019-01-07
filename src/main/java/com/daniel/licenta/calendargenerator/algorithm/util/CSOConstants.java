package com.daniel.licenta.calendargenerator.algorithm.util;

public final class CSOConstants {
    public static final int MAX_NUMBER_OF_STUDENT_CLASSES = 50;
    public static final int MAX_NUMBER_OF_TEACHERS = 100;
    // mixture ratio - percentage of CATS in tracing mode
    public static final int MR = 4;
    public static final int CATS = 30;
    public static final int ITERATIONS = 1000;
    public static final int REFINEMENT_STEPS = 50000;
    public static final double MAX_FITNESS = 10000000;
    public static final int HARD_CONSTRAINT_WEIGHT = 10;
    public static final double BASE = 1.3;

    // PARAMETERS FOR SEEKING MODE
    public static final int SEEKING_MEMORY_POOL = 2;
    public static final int SEEKING_RANGE_DIMENSION = 10;
    // self position consideration - if it equals to 1 the cat will also consider its current position during catSeek procedure
    public static final int SPC = 1;
    // count of dimensions to change - percentage of dimensions to change
    public static final int CDC = 10;

    public static final String EMPTY_TIME_SLOT = "EMPTY_TIME_SLOT";
}