package com.daniel.licenta.calendargenerator.algorithm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class CalendarData {

    public int numberOfSlotsPerDay;
    public int totalNumberOfStudentClasses;
    public int totalNumberOfTeachers;

    public List<CourseGroupRelationshipRecord> courseGroupRelationshipRecords = new ArrayList<>();
    public Teacher[] teachers;
    public StudentGroup[] studentGroups;
    public RoomRecord[] rooms;

    public CalendarData(int totalNumberOfStudentClasses, int totalNumberOfTeachers) {
        this.totalNumberOfStudentClasses = totalNumberOfStudentClasses;
        this.totalNumberOfTeachers = totalNumberOfTeachers;
    }

    public List<RoomRecord> getAllRoomsByCapacity(int capacity) {
        return Arrays.stream(rooms).filter(roomRecord -> roomRecord.capacity == capacity).collect(Collectors.toList());
    }

    public Teacher getTeacherByIndex(int index) {
        return teachers[index];
    }
}
