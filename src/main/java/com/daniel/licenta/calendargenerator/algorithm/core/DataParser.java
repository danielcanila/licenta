package com.daniel.licenta.calendargenerator.algorithm.core;

import com.daniel.licenta.calendargenerator.algorithm.util.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.daniel.licenta.calendargenerator.algorithm.core.ConfigCSO.*;
import static com.daniel.licenta.calendargenerator.algorithm.core.ConfigCSO.HOURS_IN_WEEK;

@Component
public class DataParser {

    @Autowired
    private FitnessCalculator fitnessCalculator;

    @Autowired
    private RandomGenerator randomGenerator;

    public void displayResults(int[][][] cat) {

        System.out.println("-------------------------------------------------------------------\n");

        double a1 = fitnessCalculator.calculateTeacherUnavailabilityFitness(0, HOURS_IN_WEEK, cat);
        double a2 = fitnessCalculator.calculateParallelTeachingFitness(0, HOURS_IN_WEEK, cat);
        double a3 = fitnessCalculator.calculateUnassignedStudentClassPeriodFitness(0, HOURS_IN_WEEK, cat);

        System.out.print("\nSoft constraints\n");

        double a5 = fitnessCalculator.calculateTeacherDispersionFitnessSoft(HOURS_IN_WEEK, cat, ITDW);
        double a7 = fitnessCalculator.calculateTeacherEmptyPeriodsFitnessSoft(0, HOURS_IN_WEEK, cat, TEPW);

        System.out.println("SEED : " + randomGenerator.getSeed());
        System.out.printf("\nCOST SUMMARY (as calculated for emptyPeriodWeight=%.2f, teacherDispersionWeight=%.2f\n", TEPW, ITDW);
        System.out.print("-------------------------------------------------------------------\n");
        System.out.printf("\nCost of unavailability : %f", a1);
        System.out.printf("\nCost of parallel teaching : %f", a2);
        System.out.printf("\nCost of class empty periods :%f", a3);
        System.out.printf("\nCost of teachers dispersion : %f", a5);
        System.out.printf("\nCost of teachers empty periods : %f\n", a7);
        System.out.printf("\nT o t a l   c o s t   i s   %f \n", a1 + a2 + a3 + a5 + a7);
    }

}
