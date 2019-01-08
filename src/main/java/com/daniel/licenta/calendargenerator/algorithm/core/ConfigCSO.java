package com.daniel.licenta.calendargenerator.algorithm.core;

import com.daniel.licenta.calendargenerator.algorithm.inputmodel.ConfigData;
import org.springframework.stereotype.Component;

@Component
public class ConfigCSO {

    public static int MR = 4;
    public static int CATS = 30;
    public static int ITERATIONS = 500;
    public static int REFINEMENT_STEPS = 50000;
    public static double MAX_FITNESS = 10000000;
    public static int HARD_CONSTRAINT_WEIGHT = 10;
    public static int SEEKING_MEMORY_POOL = 2;
    public static int SEEKING_RANGE_DIMENSION = 10;
    // self position consideration - if it equals to 1 the cat will also consider its current position during catSeek procedure
    public static int SPC = 1;
    // count of dimensions to change - percentage of dimensions to change
    public static int CDC = 10;

    public static int HOURS_IN_WEEK = 30;
    public static int HOURS_PER_DAY = 6;
    public static int SEED = 1534380238;
    public static double TEPW = 0.06;
    public static double ITDW = 1.0;
    public static double ICDW = 0.95;
    public static double TEPW_O = 1.35;
    public static double ITDW_O = 0.06;
    public static double ICDW_O = 0.06;

    public void overrideConfigData(ConfigData configData) {
        nullCheckSetting(configData.mr, () -> {
            ConfigCSO.MR = configData.mr;
        });
        nullCheckSetting(configData.cats, () -> {
            ConfigCSO.CATS = configData.cats;
        });
        nullCheckSetting(configData.iterations, () -> {
            ConfigCSO.ITERATIONS = configData.iterations;
        });
        nullCheckSetting(configData.refinementSteps, () -> {
            ConfigCSO.REFINEMENT_STEPS = configData.refinementSteps;
        });
        nullCheckSetting(configData.maxFitness, () -> {
            ConfigCSO.MAX_FITNESS = configData.maxFitness;
        });
        nullCheckSetting(configData.hardConstraintWeight, () -> {
            ConfigCSO.HARD_CONSTRAINT_WEIGHT = configData.hardConstraintWeight;
        });
        nullCheckSetting(configData.seekingMemoryPool, () -> {
            ConfigCSO.SEEKING_MEMORY_POOL = configData.seekingMemoryPool;
        });
        nullCheckSetting(configData.seekingRangeDimension, () -> {
            ConfigCSO.SEEKING_RANGE_DIMENSION = configData.seekingRangeDimension;
        });
        nullCheckSetting(configData.hoursInWeek, () -> {
            ConfigCSO.HOURS_IN_WEEK = configData.hoursInWeek;
        });
        nullCheckSetting(configData.hoursPerDay, () -> {
            ConfigCSO.HOURS_PER_DAY = configData.hoursPerDay;
        });
        nullCheckSetting(configData.seed, () -> {
            ConfigCSO.SEED = configData.seed;
        });
        nullCheckSetting(configData.emptyPeriodWeight, () -> {
            ConfigCSO.TEPW = configData.emptyPeriodWeight;
        });
        nullCheckSetting(configData.teacherDispersionWeight, () -> {
            ConfigCSO.ITDW = configData.teacherDispersionWeight;
        });
        nullCheckSetting(configData.classDispersionWeight, () -> {
            ConfigCSO.ICDW = configData.classDispersionWeight;
        });
        nullCheckSetting(configData.emptyPeriodWeightOptimizationPhase, () -> {
            ConfigCSO.TEPW_O = configData.emptyPeriodWeightOptimizationPhase;
        });
        nullCheckSetting(configData.teacherDispersionWeightOptimizationPhase, () -> {
            ConfigCSO.ITDW_O = configData.teacherDispersionWeightOptimizationPhase;
        });
        nullCheckSetting(configData.classDispersionWeight, () -> {
            ConfigCSO.ICDW_O = configData.classDispersionWeightOptimizationPhase;
        });

    }

    private void nullCheckSetting(Object field, Runnable run) {
        if (field != null) {
            run.run();
        }
    }
}
