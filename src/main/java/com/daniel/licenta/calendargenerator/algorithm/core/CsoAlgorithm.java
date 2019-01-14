package com.daniel.licenta.calendargenerator.algorithm.core;


import com.daniel.licenta.calendargenerator.algorithm.model.CalendarData;
import com.daniel.licenta.calendargenerator.algorithm.util.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.daniel.licenta.calendargenerator.algorithm.core.ConfigCSO.*;

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
    private LocalSearchOptimizerCSO optimizerCSO;

    @Autowired
    private ConfigCSO configCSO;

    public int[][][] runCSO(CalendarData calendarData) {
        randomGenerator.setSeed(configCSO.SEED);
        fitnessCalculator.setCalendarData(calendarData);

        int[][][] globalBestCat = mainCSO.runCsoCoreAlgorithm(TEPW, ITDW, calendarData);

        dataParser.displayResults(globalBestCat);

        globalBestCat = optimizerCSO.runOptimizationPhase(TEPW_O, ITDW_O, calendarData, globalBestCat, REFINEMENT_STEPS, true);

        dataParser.displayResults(globalBestCat);

        return globalBestCat;
    }


}