package com.daniel.licenta.calendargenerator.algorithm.model;

import static com.daniel.licenta.calendargenerator.algorithm.util.CSOConstants.EMPTY_TIME_SLOT;
import static com.daniel.licenta.calendargenerator.algorithm.util.CSOConstants.MAX_NUMBER_OF_STUDENT_CLASSES;

public class Teacher {

    public Teacher(int hoursInWeek) {
        unavailableTimeslots = new int[5 * hoursInWeek];
        for (int i = 0; i < unavailableTimeslots.length; i++) {
            unavailableTimeslots[i] = -1;
        }

        isAvailableAtDay = new int[5];
        for (int i = 0; i < isAvailableAtDay.length; i++) {
            isAvailableAtDay[i] = 1;
        }
    }

    public String surname;
    public int totalHours; // total teaching hours of teacher

    public int availabilityHours; // number of hours during which the teacher is available
    public int availableDays; // number of days druing which the teacher is available
    public int[] isAvailableAtDay; // it is 1 if the teacher is available at that day, else it is -1
    public int[] unavailableTimeslots; // it is 1 if the teacher is not available at that timeslot, else it is -1

    public int numberOfClasses = 0; // number of studentGroups
    public int[][] classesHeTeaches = new int[MAX_NUMBER_OF_STUDENT_CLASSES][3]; // contains the studentGroups, the hours and the lessons of the teacher a

    public boolean isEmptyTimeSlot() {
        return surname.equals(EMPTY_TIME_SLOT);
    }

    public int getCapacityOfClass(int classIndex) {
        for (int[] classesHeTeach : classesHeTeaches) {
            if (classesHeTeach[0] == classIndex) {
                return classesHeTeach[3];
            }
        }
        throw new RuntimeException("No class present!");
    }
}
