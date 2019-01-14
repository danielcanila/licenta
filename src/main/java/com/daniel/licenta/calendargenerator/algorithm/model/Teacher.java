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
    public int totalHours;

    public int availabilityHours;
    public int availableDays;
    // it is 1 if the teacher is available at that day, else it is -1
    public int[] isAvailableAtDay;
    // it is 1 if the teacher is not available at that timeslot, else it is -1
    public int[] unavailableTimeslots;
    // number of studentGroups
    public int numberOfClasses = 0;
    // contains the studentGroups, the hours and the lessons of the teacher a
    public int[][] classesHeTeaches = new int[MAX_NUMBER_OF_STUDENT_CLASSES][3];

    public boolean isEmptyTimeSlot() {
        return surname.equals(EMPTY_TIME_SLOT);
    }

}
