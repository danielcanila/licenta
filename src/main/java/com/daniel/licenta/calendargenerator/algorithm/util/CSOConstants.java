package com.daniel.licenta.calendargenerator.algorithm.util;

public final class CSOConstants {
    public static final int MAX_NUMBER_OF_STUDENT_CLASSES = 50; // maximum number of studentGroups
    public static final int MAX_NUMBER_OF_TEACHERS = 100; // maximum number of teachers
    public static final int MR = 4; // mixture ratio - percentage of CATS in tracing mode
    public static final int CATS = 30; // number of CATS
    public static final int ITERATIONS = 1000; // number of ITERATIONS of core process
    public static final int REFINEMENT_STEPS = 50000;//0;
    public static final double MAX_VALUE = 10000000; // worst (maximum) value of the fitness function
    public static final int HARD_CONSTRAINT_WEIGHT = 10;
    public static final double BASE = 1.3; // exponential growth base

    // PARAMETERS FOR SEEKING MODE
    public static final int SEEKING_MEMORY_POOL = 2; // seeking memory pool - number of positions to consider during catSeek procedure
    public static final int SEEKING_RANGE_DIMENSION = 10; // seeking range of the selected dimension - percentage of change of each dimension
    public static final int SPC = 1; // self position consideration - if it equals to 1 the cat will also consider its current position during catSeek procedure
    public static final int CDC = 10; // count of dimensions to change - percentage of dimensions to change

    public static final String EMPTY_TIME_SLOT = "EMPTY_TIME_SLOT";
}