package com.daniel.licenta.calendargenerator.algorithm.core;

import com.daniel.licenta.calendargenerator.algorithm.model.CalendarData;
import com.daniel.licenta.calendargenerator.algorithm.model.CourseGroupRelationshipRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.daniel.licenta.calendargenerator.algorithm.util.CSOConstants.BASE;
import static com.daniel.licenta.calendargenerator.algorithm.core.ConfigCSO.HARD_CONSTRAINT_WEIGHT;

@Component
public class FitnessCalculator {

    @Autowired
    private ConfigCSO configCSO;

    private CalendarData calendarData;

    public void setCalendarData(CalendarData calendarData) {
        this.calendarData = calendarData;
    }

    void swap(int[][][] cat, int classPosition, int timeslot1, int timeslot2) {
        int temp = cat[classPosition][timeslot1][0];
        cat[classPosition][timeslot1][0] = cat[classPosition][timeslot2][0];
        cat[classPosition][timeslot2][0] = temp;
    }

    double calculateTeacherEmptyPeriodsFitness(int begin, int end, int[][][] cat, double TEPW1, int showResults) {
        double total_cost = 0.0;
        int cases_of_teachers = 0;
        int total_gaps_of_teachers = 0;

        for (int i = 0; i < calendarData.totalNumberOfTeachers; i++) {
            if (calendarData.teachers[i].isEmptyTimeSlot()) {
                continue;
            }

            double cost = 0.0;
            int total_gaps = 0;
            int days = 0;

            for (int start = begin; start < end; start = start + configCSO.HOURS_PER_DAY) {
                int gaps = 0;
                int teaching_hours = 0;
                int last_lesson = start;
                int first_lesson = start;
                int flag = 1;
                int has_lesson = 0;
                for (int t = start; t < start + configCSO.HOURS_PER_DAY; t++) {
                    for (int j = 0; j < calendarData.teachers[i].numberOfClasses; j++) {
                        if (cat[calendarData.teachers[i].classesHeTeaches[j][0]][t][0] == i) {
                            has_lesson = 1;
                            if (flag == 1) {
                                first_lesson = t;
                                flag = 0;
                            }
                            last_lesson = t;
                            teaching_hours++;
                            break;
                        }
                    }
                }

                if (has_lesson == 1) {
                    gaps = last_lesson - first_lesson + 1 - teaching_hours;
                    total_gaps = total_gaps + gaps;
                }

                if (gaps > 0) {
                    total_gaps_of_teachers = total_gaps_of_teachers + gaps;
                    days++;
                }
            }
            if (days > 0) {
                cases_of_teachers++;
                cost = TEPW1 * total_gaps * Math.pow(BASE, days);
            }

            if (showResults == 1) {
                System.out.printf("\nTeacher %s has %d days with total gaps: %d\n", calendarData.teachers[i].surname, days, total_gaps);
            }

            total_cost = total_cost + cost;
        }

        if (showResults == 1 || showResults == 2) {
            System.out.printf("There are %d teachers with  %d total idle  timeslots\n", cases_of_teachers, total_gaps_of_teachers);
        }
        return total_cost;


    }

    // calculates and returns the ideal daily dispersion of teaching hours of the teacher
    private double calculateIdealTeacherDispersion(int teacherPosition, int totalHours) {
        return configCSO.HOURS_PER_DAY * totalHours / (double) calendarData.teachers[teacherPosition].availabilityHours;
    }

    private int calculateCeilNumber(int teacherNumber, int totalHours) {
        return totalHours % calendarData.teachers[teacherNumber].availableDays;
    }

    private int calculateFloorNumber(int teacherNumber, int totalHours) {
        double ideal = calculateIdealTeacherDispersion(teacherNumber, totalHours);
        double floor1 = Math.floor(ideal);
        if (floor1 == 0) {
            return 0;
        } else {
            return calendarData.teachers[teacherNumber].availableDays - (totalHours % calendarData.teachers[teacherNumber].availableDays);
        }
    }

    // calculates the dispersion of teaching hours of the teacher and returns the relevant cost
    double calculateTeacherDispersionFitness(int end, int[][][] cat, double IDTW, int showResults) {
        int[] idealDistrTable = new int[5];
        int[] actualDistTable = new int[5];
        int totalNumberOfWrongTeachers = 0;
        int totalNumberOfFaultDays = 0;
        double total_cost = 0.0;

        for (int teacherPosition = 0; teacherPosition < calendarData.totalNumberOfTeachers; teacherPosition++) {
            if (calendarData.teachers[teacherPosition].isEmptyTimeSlot()) {
                continue;
            }
            double absolute_error = 0.0;
            double ideal = calculateIdealTeacherDispersion(teacherPosition, calendarData.teachers[teacherPosition].totalHours);
            double ceil_no = calculateCeilNumber(teacherPosition, calendarData.teachers[teacherPosition].totalHours);
            double floor_no = calculateFloorNumber(teacherPosition, calendarData.teachers[teacherPosition].totalHours);

            for (int start = 0; start < end; start = start + configCSO.HOURS_PER_DAY) {
                int hours_per_day = 0;
                for (int j = 0; j < calendarData.teachers[teacherPosition].numberOfClasses; j++) {
                    int hours_per_day_of_class = 0;
                    int class1 = calendarData.teachers[teacherPosition].classesHeTeaches[j][0];

                    for (int t = start; t < start + configCSO.HOURS_PER_DAY; t++) {
                        if (cat[class1][t][0] == teacherPosition) {
                            hours_per_day_of_class++;
                        }
                    }
                    hours_per_day = hours_per_day + hours_per_day_of_class;
                }

                actualDistTable[start / configCSO.HOURS_PER_DAY] = hours_per_day;
            }

            for (int j = 0; j < 5; j++) {
                if (calendarData.teachers[teacherPosition].isAvailableAtDay[j] == -1) {
                    idealDistrTable[j] = -1;
                } else {
                    if (floor_no > 0) {
                        idealDistrTable[j] = (int) Math.floor(ideal);
                        floor_no--;
                        continue;
                    }
                    if (ceil_no > 0) {
                        idealDistrTable[j] = (int) Math.ceil(ideal);
                        ceil_no--;
                        continue;
                    }
                    if (floor_no == 0 && ceil_no == 0) {
                        idealDistrTable[j] = 0;
                    }
                }
            }

            for (int ii = 0; ii < 5; ii++) {
                for (int j = 0; j < 5; j++) {
                    if (actualDistTable[ii] == 0 && idealDistrTable[j] == -1 && ii == j) {
                        actualDistTable[ii] = -2;
                        idealDistrTable[j] = -2;
                        break;
                    }
                    if (actualDistTable[ii] == idealDistrTable[j]) {
                        actualDistTable[ii] = -2;
                        idealDistrTable[j] = -2;
                        break;
                    }
                }
            }

            int faultDays = 0;
            for (int ii = 0; ii < 5; ii++) {
                if (actualDistTable[ii] == -2) {
                    continue;
                }
                int diff1 = 50;
                int diff2 = 100;

                int index = -1;
                for (int j = 0; j < 5; j++) {
                    if (idealDistrTable[j] == -2) {
                        continue;
                    }
                    if (idealDistrTable[j] == -1) {
                        idealDistrTable[j] = 0;
                    }
                    diff2 = Math.abs(actualDistTable[ii] - idealDistrTable[j]);
                    if (diff2 < diff1) {
                        diff1 = diff2;
                        index = j;
                    }
                }

                if (diff1 != 0) {
                    absolute_error = absolute_error + diff1;
                    faultDays++;
                    idealDistrTable[index] = -2;
                }
            }

            if (faultDays > 0) {
                totalNumberOfFaultDays = totalNumberOfFaultDays + faultDays;
                total_cost += absolute_error * Math.pow(BASE, (double) faultDays);
                totalNumberOfWrongTeachers++;

                if (showResults == 1) {
                    System.out.printf("\nTeacher %s has %d days with wrong dispersion\n", calendarData.teachers[teacherPosition].surname, faultDays);
                    System.out.print("-----------------------------------------------------------------------------\n");
                }

            }
        }

        if (showResults == 1 || showResults == 2) {
            System.out.printf("Total cases of Teachers wrong dispersion are %d. The number of these days are %d\n", totalNumberOfWrongTeachers, totalNumberOfFaultDays);
        }


        return IDTW * total_cost;
    }

    // calculates the dispersion of lessons of each class and returns the relevant cost
    double calculateClassDispersionFitness(int end, int[][][] cat, double ICDW1, int showResults) {
        double total_cost = 0.0;
        int violation_cases = 0;
        int total_problem_days = 0;

        for (int i = 0; i < calendarData.totalNumberOfStudentClasses; i++) {
            int problem_days = 0;
            double cost = 0.0;
            int totalHoursPerClass = 0;
            for (int start = 0; start < end; start = start + configCSO.HOURS_PER_DAY) {
                int hours = 0;
                for (int k = 0; k < calendarData.studentGroups[i].numberOfTeachers; k++) {
                    int hours_per_day_of_teacher = 0;
                    int teacher1 = calendarData.studentGroups[i].teachersOfClassAndHours[k][0];

                    for (int t = start; t < start + configCSO.HOURS_PER_DAY; t++) {
                        if ((cat[i][t][0] == teacher1)) {
                            hours_per_day_of_teacher++;
                        }
                    }

                    if (calendarData.studentGroups[i].teachersOfClassAndHours[k][2] > 0 && hours_per_day_of_teacher > calendarData.studentGroups[i].teachersOfClassAndHours[k][2]) {
                        hours += hours_per_day_of_teacher - calendarData.studentGroups[i].teachersOfClassAndHours[k][2];
                    }
                }

                if (hours > 0) {
                    totalHoursPerClass = totalHoursPerClass + hours;
                    problem_days++;
                }
            }

            if (problem_days > 0) {
                total_problem_days = total_problem_days + problem_days;
                violation_cases++;

                cost = ICDW1 * Math.pow(BASE, violation_cases);

                if (showResults == 1) {
                    System.out.printf("\nThe class %s has %d repeated lessons at %d  days\n", calendarData.studentGroups[i].
                            className, totalHoursPerClass, problem_days);
                    System.out.print("---------------------------------------------------------------------------------\n");
                }
            }
            total_cost += cost;
        }

        if (showResults == 1 || showResults == 2) {
            System.out.printf("The studentGroups with  wrong dispersion are %d. The days it occurs are %d\n", violation_cases, total_problem_days);
        }
        return total_cost;
    }

    double calculateParallelRoomFitness(int[][][] cat, int showResults) {
        int[][] roomData = new int[calendarData.rooms.length][configCSO.HOURS_IN_WEEK];

        for (int i = 0; i < calendarData.rooms.length; i++) {
            for (int j = 0; j < configCSO.HOURS_IN_WEEK; j++) {
                roomData[i][j] = 0;
            }
        }

        for (int i = 0; i < calendarData.totalNumberOfStudentClasses; i++) {
            for (int j = 0; j < configCSO.HOURS_IN_WEEK; j++) {
                roomData[cat[i][j][1]][j]++;
            }
        }

        int duplicates = 0;
        for (int i = 0; i < calendarData.rooms.length; i++) {
            for (int j = 0; j < configCSO.HOURS_IN_WEEK; j++) {
                if (roomData[i][j] > 1) {
                    duplicates++;
                }
            }
        }
        if (showResults == 1) {
            System.out.printf("Number of parallel rooms %d\n", duplicates);
        }

        if (duplicates > 0) {
            return HARD_CONSTRAINT_WEIGHT * Math.pow(BASE, duplicates - 1);
        }
        return 0;
    }

    // calculates if there are parallel teachings and returns the relevant cost
    double calculateParallelTeachingFitness(int start, int end, int[][][] cat, int showResults) {
        int number_of_cases = 0;
        double cost = 0.0;

        for (int i = 0; i < calendarData.totalNumberOfTeachers; i++) {
            if (calendarData.teachers[i].isEmptyTimeSlot()) {
                continue;
            }
            for (int t = start; t < end; t++) {
                int parallel_teaching = 0;
                for (int j = 0; j < calendarData.totalNumberOfStudentClasses; j++) {
                    if (cat[j][t][0] == i) {
                        parallel_teaching++;
                    }
                }

                if (parallel_teaching > 1) {
                    number_of_cases++;
                    cost += HARD_CONSTRAINT_WEIGHT * Math.pow(BASE, parallel_teaching - 1);
                }
            }
        }

        for (CourseGroupRelationshipRecord relationship : calendarData.getCourseGroupRelationshipRecords()) {
            int[][] semianSchedule = cat[relationship.getSemianIndex()];
            int parallel_teaching = 0;
            for (Integer subGroup : relationship.getStudentGroup()) {
                int[][] subgroupSchedule = cat[subGroup];
                for (int hour = 0; hour < configCSO.HOURS_IN_WEEK; hour++) {
                    int[] scheduleAtHourForSemian = semianSchedule[hour];
                    int[] scheduleAtHourForSubgroup = subgroupSchedule[hour];

                    if ((!calendarData.getTeacherByIndex(scheduleAtHourForSemian[0]).isEmptyTimeSlot() &&
                            !calendarData.getTeacherByIndex(scheduleAtHourForSubgroup[0]).isEmptyTimeSlot())) {
                        parallel_teaching++;
                        number_of_cases++;
                    }
                }
            }
            if (parallel_teaching > 0) {
                cost += HARD_CONSTRAINT_WEIGHT * Math.pow(BASE, parallel_teaching);
            }
        }

        if (showResults == 1) {
            System.out.printf("Total cases of teacher parallel teaching are %d\n", number_of_cases);
        }

        return cost;
    }

    // calculates the empty periods of each class and returns the relevant cost
    double calculateUnassignedStudentClassPeriodFitness(int begin, int end, int[][][] cat, int showResults) {
        int number_of_cases = 0;
        double cost = 0.0;

        for (int i = 0; i < calendarData.totalNumberOfStudentClasses; i++) {
            for (int start = begin; start < end; start = start + configCSO.HOURS_PER_DAY) {
                for (int t = start; t < start + configCSO.HOURS_PER_DAY; t++) {
                    if (cat[i][t][0] == -1
                            && t != configCSO.HOURS_PER_DAY - 1
                            && t != 2 * configCSO.HOURS_PER_DAY - 1
                            && t != 3 * configCSO.HOURS_PER_DAY - 1
                            && t != 4 * configCSO.HOURS_PER_DAY - 1
                            && t != 5 * configCSO.HOURS_PER_DAY - 1) {
                        number_of_cases++;
                        cost = cost + HARD_CONSTRAINT_WEIGHT * Math.pow(2 * BASE, BASE);
                        if (showResults == 1) {
                            System.out.printf("\nClass %s has empty timeslot %d\n", calendarData.studentGroups[i].className, t);
                        }
                    }
                }
            }
        }

        if (showResults == 1) {
            System.out.printf("Total cases of class empty periods are %d \n", number_of_cases);
        }
        return cost;
    }

    // calculates the unavailability of each teacher and returns the relevant cost
    double calculateTeacherUnavailabilityFitness(int start, int end, int[][][] cat, int showResults) {
        int t;
        int number_of_cases = 0;
        double cost = 0.0;
        for (int i = 0; i < calendarData.totalNumberOfStudentClasses; i++) {
            for (int j = 0; j < calendarData.studentGroups[i].numberOfTeachers; j++) {
                for (t = start; t < end; t++) {
                    if (cat[i][t][0] == calendarData.studentGroups[i].teachersOfClassAndHours[j][0]
                            && calendarData.teachers[calendarData.studentGroups[i].teachersOfClassAndHours[j][0]].unavailableTimeslots[t] == 1) {
                        number_of_cases++;
                    }
                }
            }
        }

        cost = number_of_cases * HARD_CONSTRAINT_WEIGHT * Math.pow(BASE, 4.75);
        if (showResults == 1) {
            System.out.printf("Total cases of teacher unavailability are %d\n", number_of_cases);
        }
        return cost;

    }

    double calculateFitness(int end, int[][][] cat, double TEPW, double ITDW, double ICDW) {
        double a1 = calculateTeacherUnavailabilityFitness(0, end, cat, 0);
        double a2 = calculateParallelTeachingFitness(0, end, cat, 0);
        double a3 = calculateUnassignedStudentClassPeriodFitness(0, end, cat, 0);
        double a4 = calculateParallelRoomFitness(cat, 0);
        double a5 = calculateTeacherEmptyPeriodsFitness(0, end, cat, TEPW, 0);
        double a6 = calculateTeacherDispersionFitness(end, cat, ITDW, 0);
        double a7 = calculateClassDispersionFitness(end, cat, ICDW, 0);
        return a1 + a2 + a3 + a4 + a5 + a6 + a7;
    }

    double calculatePartialFitness(int start, int end, int[][][] cat, double TEPW) {
        double a1 = calculateTeacherUnavailabilityFitness(start, end, cat, 0);
        double a2 = calculateParallelTeachingFitness(start, end, cat, 0);
        double a3 = calculateUnassignedStudentClassPeriodFitness(start, end, cat, 0);
        double a4 = calculateParallelRoomFitness(cat, 0);
        double a5 = calculateTeacherEmptyPeriodsFitness(start, end, cat, TEPW, 0);
        return a1 + a2 + a3 + a4 + a5;
    }

    double checkHardConstraints(int start, int end, int[][][] cat) {
        double a1 = calculateTeacherUnavailabilityFitness(start, end, cat, 0);
        double a2 = calculateParallelTeachingFitness(start, end, cat, 0);
        double a3 = calculateUnassignedStudentClassPeriodFitness(start, end, cat, 0);
        double a4 = calculateParallelRoomFitness(cat, 0);
        return a1 + a2 + a3 + a4;
    }

}
