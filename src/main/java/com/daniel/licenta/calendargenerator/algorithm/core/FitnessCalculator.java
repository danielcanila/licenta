package com.daniel.licenta.calendargenerator.algorithm.core;

import com.daniel.licenta.calendargenerator.algorithm.model.CalendarData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.daniel.licenta.calendargenerator.algorithm.core.ConfigCSO.HOURS_PER_DAY;
import static com.daniel.licenta.calendargenerator.algorithm.util.CSOConstants.BASE;
import static com.daniel.licenta.calendargenerator.algorithm.core.ConfigCSO.HARD_CONSTRAINT_WEIGHT;

@Component
public class FitnessCalculator {
    private CalendarData calendarData;

    public void setCalendarData(CalendarData calendarData) {
        this.calendarData = calendarData;
    }

    public double calculateFitness(int end, int[][][] cat, double TEPW, double ITDW) {
        double a1 = calculateTeacherUnavailabilityFitness(0, end, cat);
        double a2 = calculateParallelTeachingFitness(0, end, cat);
        double a3 = calculateUnassignedStudentClassPeriodFitness(0, end, cat);
        double a4 = calculateTeacherEmptyPeriodsFitnessSoft(0, end, cat, TEPW);
        double a5 = calculateTeacherDispersionFitnessSoft(end, cat, ITDW);
        return a1 + a2 + a3 + a4 + a5;
    }

    public double calculatePartialFitness(int start, int end, int[][][] cat, double TEPW) {
        double a1 = calculateTeacherUnavailabilityFitness(start, end, cat);
        double a2 = calculateParallelTeachingFitness(start, end, cat);
        double a3 = calculateUnassignedStudentClassPeriodFitness(start, end, cat);
        double a4 = calculateTeacherEmptyPeriodsFitnessSoft(start, end, cat, TEPW);
        return a1 + a2 + a3 + a4;
    }

    public double checkHardConstraints(int start, int end, int[][][] cat) {
        double a1 = calculateTeacherUnavailabilityFitness(start, end, cat);
        double a2 = calculateParallelTeachingFitness(start, end, cat);
        double a3 = calculateUnassignedStudentClassPeriodFitness(start, end, cat);
        return a1 + a2 + a3;
    }

    public void swap(int[][][] cat, int classPosition, int timeslotOne, int timeslotTwo) {
        int temp = cat[classPosition][timeslotOne][0];
        cat[classPosition][timeslotOne][0] = cat[classPosition][timeslotTwo][0];
        cat[classPosition][timeslotTwo][0] = temp;
    }

    // fitness functions start
    public double calculateTeacherUnavailabilityFitness(int start, int end, int[][][] cat) {
        int t;
        int number_of_cases = 0;
        double cost = 0.0;
        for (int i = 0; i < calendarData.studentCount; i++) {
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
        return cost;

    }

    public double calculateParallelTeachingFitness(int start, int end, int[][][] cat) {
        double cost = 0.0;

        for (int i = 0; i < calendarData.teacherCount; i++) {
            if (calendarData.teachers[i].isEmptyTimeSlot()) {
                continue;
            }
            for (int t = start; t < end; t++) {
                int parallel_teaching = 0;
                for (int j = 0; j < calendarData.studentCount; j++) {
                    if (cat[j][t][0] == i) {
                        parallel_teaching++;
                    }
                }

                if (parallel_teaching > 1) {
                    cost += HARD_CONSTRAINT_WEIGHT * Math.pow(BASE, parallel_teaching - 1);
                }
            }
        }

        return cost;
    }

    public double calculateUnassignedStudentClassPeriodFitness(int begin, int end, int[][][] cat) {
        double cost = 0.0;

        for (int i = 0; i < calendarData.studentCount; i++) {
            for (int start = begin; start < end; start = start + HOURS_PER_DAY) {
                for (int t = start; t < start + HOURS_PER_DAY; t++) {
                    if (cat[i][t][0] == -1
                            && t != HOURS_PER_DAY - 1
                            && t != 2 * HOURS_PER_DAY - 1
                            && t != 3 * HOURS_PER_DAY - 1
                            && t != 4 * HOURS_PER_DAY - 1
                            && t != 5 * HOURS_PER_DAY - 1) {
                        cost = cost + HARD_CONSTRAINT_WEIGHT * Math.pow(2 * BASE, BASE);
                    }
                }
            }
        }

        return cost;
    }

    public double calculateTeacherEmptyPeriodsFitnessSoft(int begin, int end, int[][][] cat, double TEPW1) {
        double total_cost = 0.0;
        int total_gaps_of_teachers = 0;

        for (int teacherPosition = 0; teacherPosition < calendarData.teacherCount; teacherPosition++) {
            if (calendarData.teachers[teacherPosition].isEmptyTimeSlot()) {
                continue;
            }
            double cost = 0.0;
            int totalGaps = 0;
            int days = 0;

            // for each day
            for (int day = begin; day < end; day = day + HOURS_PER_DAY) {
                int gaps = 0;
                int teachingHoursPerDay = 0;
                int lastLesson = day;
                int firstLesson = day;
                boolean hasLesson = false;
                for (int timeSlot = day; timeSlot < day + HOURS_PER_DAY; timeSlot++) {
                    for (int j = 0; j < calendarData.teachers[teacherPosition].numberOfClasses; j++) {
                        if (cat[calendarData.teachers[teacherPosition].classesHeTeaches[j][0]][timeSlot][0] == teacherPosition) {
                            if (!hasLesson) {
                                firstLesson = timeSlot;
                                hasLesson = true;
                            }
                            lastLesson = timeSlot;
                            teachingHoursPerDay++;
                            break;
                        }
                    }
                }

                if (hasLesson) {
                    gaps = lastLesson - firstLesson + 1 - teachingHoursPerDay;
                    totalGaps += gaps;
                }

                if (gaps > 0) {
                    total_gaps_of_teachers = total_gaps_of_teachers + gaps;
                    days++;
                }
            }
            if (days > 0) {
                cost = TEPW1 * totalGaps * Math.pow(BASE, days);
            }

            total_cost = total_cost + cost;
        }

        return total_cost;
    }

    public double calculateTeacherDispersionFitnessSoft(int end, int[][][] cat, double IDTW) {
        double totalCost = 0.0;
        for (int teacherPosition = 0; teacherPosition < calendarData.teacherCount; teacherPosition++) {
            if (calendarData.teachers[teacherPosition].isEmptyTimeSlot()) {
                continue;
            }

            int[] actualDistributionTable = createActualDistributionTable(end, cat, teacherPosition);
            int[] idealDistributionTable = createIdealDistributionTable(teacherPosition);
            compareTeacherDistributionTable(actualDistributionTable, idealDistributionTable);
            totalCost += retrieveTeacherDistributionTableCost(actualDistributionTable, idealDistributionTable);
        }

        return IDTW * totalCost;
    }
    //fitness functions end

    private double getIdealTeacherDispersion(int teacherPosition, int totalHours) {
        return HOURS_PER_DAY * totalHours / (double) calendarData.teachers[teacherPosition].availabilityHours;
    }

    private void compareTeacherDistributionTable(int[] actualDistributionTable, int[] idealDistributionTable) {
        for (int dayActual = 0; dayActual < 5; dayActual++) {
            for (int dayIdeal = 0; dayIdeal < 5; dayIdeal++) {
                if (actualDistributionTable[dayActual] == idealDistributionTable[dayIdeal]) {
                    actualDistributionTable[dayActual] = -1;
                    idealDistributionTable[dayIdeal] = -1;
                    break;
                }
            }
        }
    }

    private int getNumberOfHigherIdealValues(int teacherNumber, int totalHours) {
        return totalHours % calendarData.teachers[teacherNumber].availableDays;
    }

    private int getNumberOfBaseValues(int teacherNumber, int totalHours) {
        double ideal = getIdealTeacherDispersion(teacherNumber, totalHours);
        double floor1 = Math.floor(ideal);
        if (floor1 == 0) {
            return 0;
        } else {
            return calendarData.teachers[teacherNumber].availableDays - (totalHours % calendarData.teachers[teacherNumber].availableDays);
        }
    }

    private double retrieveTeacherDistributionTableCost(int[] actualDistributionTable, int[] idealDistributionTable) {
        int faultDays = 0;
        double totalDifference = 0.0;
        for (int dayActual = 0; dayActual < 5; dayActual++) {
            if (actualDistributionTable[dayActual] == -1) {
                continue;
            }
            int differenceOne = HOURS_PER_DAY;

            int index = -1;
            for (int dayIdeal = 0; dayIdeal < 5; dayIdeal++) {
                if (idealDistributionTable[dayIdeal] == -1) {
                    continue;
                }
                int differenceTwo = Math.abs(actualDistributionTable[dayActual] - idealDistributionTable[dayIdeal]);
                if (differenceTwo < differenceOne) {
                    differenceOne = differenceTwo;
                    index = dayIdeal;
                }
            }

            if (differenceOne != 0 && index != -1) {
                totalDifference = totalDifference + differenceOne;
                faultDays++;
                idealDistributionTable[index] = -1;
            }
        }
        if (faultDays > 0) {
            return totalDifference * Math.pow(BASE, (double) faultDays);
        }
        return 0;
    }

    private int[] createActualDistributionTable(int end, int[][][] cat, int teacherPosition) {
        int[] actualDistributionTable = new int[5];
        for (int start = 0; start < end; start = start + HOURS_PER_DAY) {
            int hoursPerDay = 0;
            for (int j = 0; j < calendarData.teachers[teacherPosition].numberOfClasses; j++) {
                int hoursPerDayOfClass = 0;
                int class1 = calendarData.teachers[teacherPosition].classesHeTeaches[j][0];

                for (int t = start; t < start + HOURS_PER_DAY; t++) {
                    if (cat[class1][t][0] == teacherPosition) {
                        hoursPerDayOfClass++;
                    }
                }
                hoursPerDay = hoursPerDay + hoursPerDayOfClass;
            }

            actualDistributionTable[start / HOURS_PER_DAY] = hoursPerDay;
        }
        return actualDistributionTable;
    }

    private int[] createIdealDistributionTable(int teacherPosition) {
        double ideal = getIdealTeacherDispersion(teacherPosition, calendarData.teachers[teacherPosition].totalHours);
        double higherValues = getNumberOfHigherIdealValues(teacherPosition, calendarData.teachers[teacherPosition].totalHours);
        double baseValues = getNumberOfBaseValues(teacherPosition, calendarData.teachers[teacherPosition].totalHours);
        int[] idealDistributionTable = new int[5];
        for (int j = 0; j < 5; j++) {
            if (calendarData.teachers[teacherPosition].isAvailableAtDay[j] == -1) {
                idealDistributionTable[j] = 0;
            } else {
                if (baseValues > 0) {
                    idealDistributionTable[j] = (int) Math.floor(ideal);
                    baseValues--;
                    continue;
                }
                if (higherValues > 0) {
                    idealDistributionTable[j] = (int) Math.ceil(ideal);
                    higherValues--;
                    continue;
                }
                if (baseValues == 0 && higherValues == 0) {
                    idealDistributionTable[j] = 0;
                }
            }
        }
        return idealDistributionTable;
    }

}
