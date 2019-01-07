package com.daniel.licenta.calendargenerator.algorithm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.daniel.licenta.calendargenerator.algorithm.util.CSOConstants.EMPTY_TIME_SLOT;

@Getter
@Setter
@AllArgsConstructor
public class CalendarData {

    public int numberOfSlotsPerDay;
    public int totalNumberOfStudentClasses;
    public int totalNumberOfTeachers;

    public Teacher[] teachers;
    public StudentGroup[] studentGroups;
    public RoomRecord[] rooms;

    public CalendarData(int totalNumberOfStudentClasses, int totalNumberOfTeachers) {
        this.totalNumberOfStudentClasses = totalNumberOfStudentClasses;
        this.totalNumberOfTeachers = totalNumberOfTeachers;
    }

    public boolean hasEmptySlots() {
        return teachers[teachers.length - 1].surname.equals(EMPTY_TIME_SLOT);
    }

    public List<RoomRecord> getAllRoomsByCapacity(int capacity) {
        return Arrays.asList(rooms).stream().filter(roomRecord -> roomRecord.capacity == capacity).collect(Collectors.toList());
    }
}
