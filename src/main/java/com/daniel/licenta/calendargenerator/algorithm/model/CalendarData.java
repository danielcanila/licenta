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
    public int studentCount;
    public int teacherCount;

    public Teacher[] teachers;
    public StudentGroup[] studentGroups;

    public CalendarData(int totalNumberOfStudentClasses, int totalNumberOfTeachers) {
        this.studentCount = totalNumberOfStudentClasses;
        this.teacherCount = totalNumberOfTeachers;
    }

}
