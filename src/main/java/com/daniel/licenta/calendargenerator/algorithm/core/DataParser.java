package com.daniel.licenta.calendargenerator.algorithm.core;

import com.daniel.licenta.calendargenerator.algorithm.model.CalendarData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.daniel.licenta.calendargenerator.algorithm.util.BufferHandlingUtil.fprintf;

@Component
public class DataParser {

    @Autowired
    private ConfigCSO configCSO;

    @Autowired
    private FitnessCalculator fitnessCalculator;

    public void displayResults(int toFile, int[][][] a) { // displays the results
        double TEPW = 0.06;
        double ITDW = 1.0;
        double ICDW = 0.95;
        double a1;
        double a2;
        double a3;
        double a4;
        double a5;
        double a6;
        double a7;

        System.out.print("\n\n");
        System.out.print("RESULTS BEFORE OPTIMIZATION");
        System.out.print("\n----------------\n");
        System.out.print("Hard constraints\n");

        fitnessCalculator.calculateTeacherUnavailabilityFitness(toFile, 0, configCSO.HOURS_IN_WEEK, a, 1);
        fitnessCalculator.calculateParallelTeachingFitness(toFile, 0, configCSO.HOURS_IN_WEEK, a, 1);
        fitnessCalculator.calculateUnassignedStudentClassPeriodFitness(toFile, 0, configCSO.HOURS_IN_WEEK, a, 1);
        fitnessCalculator.calculateParallelRoomFitness(0, configCSO.HOURS_IN_WEEK, a, 1);
        System.out.print("\nSoft constraints\n");

        fitnessCalculator.calculateTeacherDispersionFitness(toFile, 0, configCSO.HOURS_IN_WEEK, a, ITDW, 2);
        fitnessCalculator.calculateClassDispersionFitness(toFile, 0, configCSO.HOURS_IN_WEEK, a, ICDW, 2);
        fitnessCalculator.calculateTeacherEmptyPeriodsFitness(toFile, 0, configCSO.HOURS_IN_WEEK, a, TEPW, 2);

        a1 = fitnessCalculator.calculateTeacherUnavailabilityFitness(toFile, 0, configCSO.HOURS_IN_WEEK, a, 1);
        a2 = fitnessCalculator.calculateParallelTeachingFitness(toFile, 0, configCSO.HOURS_IN_WEEK, a, 1);
        a3 = fitnessCalculator.calculateUnassignedStudentClassPeriodFitness(toFile, 0, configCSO.HOURS_IN_WEEK, a, 1);
        a4 = fitnessCalculator.calculateParallelRoomFitness(0, configCSO.HOURS_IN_WEEK, a, 1);

        System.out.print("\nSoft constraints\n");

        a5 = fitnessCalculator.calculateTeacherDispersionFitness(toFile, 0, configCSO.HOURS_IN_WEEK, a, ITDW, 2);
        a6 = fitnessCalculator.calculateClassDispersionFitness(toFile, 0, configCSO.HOURS_IN_WEEK, a, ICDW, 2);
        a7 = fitnessCalculator.calculateTeacherEmptyPeriodsFitness(toFile, 0, configCSO.HOURS_IN_WEEK, a, TEPW, 2);

        System.out.printf("\nCOST SUMMARY (as calculated for TEPW=%.2f, ICDW=%.2f, ITDW=%.2f\n", TEPW, ICDW, ITDW);
        System.out.print("-------------------------------------------------------------------\n");
        System.out.printf("\nCost of unavailability : %f", a1);
        System.out.printf("\nCost of parallel teaching : %f", a2);
        System.out.printf("\nCost of class empty periods :%f", a3);
        System.out.printf("\nCost of teachers dispersion : %f", a5);
        System.out.printf("\nCost of class dispersion : %f", a6);
        System.out.printf("\nCost of teachers empty periods : %f\n", a7);
        System.out.printf("\nCost of paralel rooms : %f\n", a4);
        System.out.printf("\nT o t a l   c o s t   i s   %f \n", a1 + a2 + a3 + a5 + a6 + a7);
    }

    public CalendarData readDataFromFile(String fileName) {
        // reads and checks the integrity of the input file
//        BufferedReader fp = fopenRead(fileName);
//
//        Teacher[] teachers = initializeWithDefaultteacher_recordInstances(MAX_NUMBER_OF_TEACHERS);
//        StudentGroup[] studentGroups = initializeWithDefaultclass_recordInstances(MAX_NUMBER_OF_STUDENT_CLASSES);
//        int totalNumberOfStudentClasses;
//        int totalNumberOfTeachers;
//
//
//        //DUBIOS
//        String[] fscanf1 = readLineFromBufferAndSplitIt(fp, "%s %s");
//        String number_of_classes1 = fscanf1[0];
//        String number_of_teachers = fscanf1[1];
//
//        totalNumberOfStudentClasses = Integer.parseInt(number_of_classes1);
//        totalNumberOfTeachers = Integer.parseInt(number_of_teachers);
//
//        if (totalNumberOfStudentClasses <= 0 || totalNumberOfStudentClasses > MAX_NUMBER_OF_STUDENT_CLASSES) {
//            System.out.print("\nInvalid number of studentGroups. Program terminated.");
//            System.exit(1);
//        }
//
//        if (totalNumberOfTeachers < 2 || totalNumberOfTeachers > MAX_NUMBER_OF_TEACHERS) {
//            System.out.print("\nInvalid number of teachers. Program terminated.");
//            System.exit(1);
//        }
//
//        for (int i = 0; i < totalNumberOfTeachers; i++) {
//            teachers[i].availabilityHours = CSOConstants.configCSO.HOURS_IN_WEEK;
//        }
//
//        for (int i = 0; i < totalNumberOfStudentClasses; i++) {
//            studentGroups[i].numberOfTeachers = 0;
//        }
//
//        for (int i = 0; i < totalNumberOfStudentClasses; i++) {
//            // DUBIOS
//            String[] fscanf2 = readLineFromBufferAndSplitIt(fp, "%s     %d");
//            studentGroups[i].className = fscanf2[0];
//            studentGroups[i].classSequence = i;
//        }
//        readLineFromBufferAndSplitIt(fp, "%d %d ");
//        for (int i = 0; i < totalNumberOfTeachers; i++) {
//            // DUBIOS
//            String[] fscanf2 = readLineFromBufferAndSplitIt(fp, " %s  %d");
//            System.out.println(java.util.Arrays.toString(fscanf2));
//            teachers[i].surname = fscanf2[0];
//
//            teachers[i].numberOfClasses = 0;
//            teachers[i].totalHours = 0;
//
//            // DUBIOS
//            String[] fscanf3 = readLineFromBufferAndSplitIt(fp, "%d");
//            System.out.println(java.util.Arrays.toString(fscanf3));
//            int b = Integer.parseInt(fscanf3[0]);
//
//            int numberOfClasses = 0;
//
//            while (b != -1) {
//
//                // DUBIOS
//                int h = Integer.parseInt(fscanf3[1]);
//                int lessons = Integer.parseInt(fscanf3[2]);
//
//                teachers[i].totalHours = teachers[i].totalHours + h;
//                teachers[i].classesHeTeaches[numberOfClasses][0] = b;
//                studentGroups[b].teachersOfClassAndHours[studentGroups[b].numberOfTeachers][0] = i;
//                studentGroups[b].teachersOfClassAndHours[studentGroups[b].numberOfTeachers][1] = h;
//                studentGroups[b].teachersOfClassAndHours[studentGroups[b].numberOfTeachers][2] = lessons;
//                studentGroups[b].numberOfTeachers++;
//                teachers[i].classesHeTeaches[numberOfClasses][1] = h;
//                teachers[i].classesHeTeaches[numberOfClasses][2] = lessons;
//
//                numberOfClasses++;
//                teachers[i].numberOfClasses = numberOfClasses;
//
//                // DUBIOS
//                fscanf3 = readLineFromBufferAndSplitIt(fp, "%d", b);
//                System.out.println(java.util.Arrays.toString(fscanf3));
//                b = Integer.parseInt(fscanf3[0]);
//
//            }
//
//            for (int j = 0; j < configCSO.HOURS_IN_WEEK; j++) {
//                teachers[i].unavailableTimeslots[j] = -1;
//            }
//
//            for (numberOfClasses = 0; numberOfClasses < 5; numberOfClasses++) {
//                teachers[i].isAvailableAtDay[numberOfClasses] = 1;
//            }
//
//            // DUBIOS
//            String[] fscanf4 = readLineFromBufferAndSplitIt(fp, "%d");
//            System.out.println(java.util.Arrays.toString(fscanf4));
//            int h = Integer.parseInt(fscanf4[0]);
//
//            if (h != -1) {
//                for (String s : fscanf4) {
//                    teachers[i].unavailableTimeslots[Integer.parseInt(s)] = 1;
//                    teachers[i].availabilityHours--;
//
//                }
//                readLineFromBufferAndSplitIt(fp, "%d", h);
//            }
//
//            teachers[i].availableDays = 5;
//            for (int start = 0; start < 29; start = start + HOURS_PER_DAY) {
//                int subtract_day = 0;
//                for (int t = start; t < start + HOURS_PER_DAY; t++) {
//                    if (teachers[i].unavailableTimeslots[t] == 1) {
//                        subtract_day++;
//                    }
//
//                    if (subtract_day == HOURS_PER_DAY) {
//                        teachers[i].availableDays--;
//                        teachers[i].isAvailableAtDay[start / HOURS_PER_DAY] = -1;
//                    }
//                }
//            }
//        }
//
//        try {
//            fp.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        return new CalendarData(totalNumberOfStudentClasses, totalNumberOfTeachers, teachers, studentGroups);
        return null;
    }

}
