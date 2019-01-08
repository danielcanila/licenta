package com.daniel.licenta.calendargenerator.algorithm.core;

import com.daniel.licenta.calendargenerator.algorithm.model.CalendarData;
import com.daniel.licenta.calendargenerator.algorithm.util.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataParser {

    @Autowired
    private ConfigCSO configCSO;

    @Autowired
    private FitnessCalculator fitnessCalculator;

    @Autowired
    private RandomGenerator randomGenerator;


    public void displayResults(int[][][] cat) {
        double TEPW = 0.06;
        double ITDW = 1.0;
        double ICDW = 0.95;
        System.out.println("-------------------------------------------------------------------\n");

        double a1 = fitnessCalculator.calculateTeacherUnavailabilityFitness(0, configCSO.HOURS_IN_WEEK, cat, 1);
        double a2 = fitnessCalculator.calculateParallelTeachingFitness(0, configCSO.HOURS_IN_WEEK, cat, 1);
        double a3 = fitnessCalculator.calculateUnassignedStudentClassPeriodFitness(0, configCSO.HOURS_IN_WEEK, cat, 1);
        double a4 = fitnessCalculator.calculateParallelRoomFitness(cat, 1);

        System.out.print("\nSoft constraints\n");

        double a5 = fitnessCalculator.calculateTeacherDispersionFitness(configCSO.HOURS_IN_WEEK, cat, ITDW, 2);
        double a6 = fitnessCalculator.calculateClassDispersionFitness(configCSO.HOURS_IN_WEEK, cat, ICDW, 2);
        double a7 = fitnessCalculator.calculateTeacherEmptyPeriodsFitness(0, configCSO.HOURS_IN_WEEK, cat, TEPW, 2);

        System.out.println("SEED : " + randomGenerator.getSeed());
        System.out.printf("\nCOST SUMMARY (as calculated for TEPW=%.2f, ICDW=%.2f, ITDW=%.2f\n", TEPW, ICDW, ITDW);
        System.out.print("-------------------------------------------------------------------\n");
        System.out.printf("\nCost of unavailability : %f", a1);
        System.out.printf("\nCost of parallel teaching : %f", a2);
        System.out.printf("\nCost of class empty periods :%f", a3);
        System.out.printf("\nCost of teachers dispersion : %f", a5);
        System.out.printf("\nCost of class dispersion : %f", a6);
        System.out.printf("\nCost of teachers empty periods : %f\n", a7);
        System.out.printf("\nCost of paralel rooms : %f\n", a4);
        System.out.printf("\nT o t a l   c o s t   i s   %f \n", a1 + a2 + a3 + a4 + a5 + a6 + a7);
    }

}
