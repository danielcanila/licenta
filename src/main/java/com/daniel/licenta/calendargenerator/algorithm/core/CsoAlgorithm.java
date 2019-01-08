package com.daniel.licenta.calendargenerator.algorithm.core;


import com.daniel.licenta.calendargenerator.algorithm.model.CalendarData;
import com.daniel.licenta.calendargenerator.algorithm.util.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CsoAlgorithm {

    @Autowired
    private FitnessCalculator fitnessCalculator;

    @Autowired
    private RandomGenerator randomGenerator;

    @Autowired
    private MainCSO mainCSO;

    @Autowired
    private DataParser dataParser;

    @Autowired
    private OptimizerCSO optimizerCSO;

    @Autowired
    private ConfigCSO configCSO;

    public int[][][] runCSO(CalendarData calendarData) {
        randomGenerator.setSeed(configCSO.SEED);
        fitnessCalculator.setCalendarData(calendarData);

        int[][][] globalBestCat = mainCSO.runCsoCoreAlgorithm(0.06, 1.0, 0.95, calendarData);

        dataParser.displayResults(globalBestCat);

        globalBestCat = optimizerCSO.runOptimizationPhase(1.35, 0.06, 0.06, calendarData, globalBestCat);

        dataParser.displayResults(globalBestCat);

        return globalBestCat;
    }


}