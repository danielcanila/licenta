package com.daniel.licenta.calendargenerator.algorithm.inputmodel;

import javafx.util.Pair;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode(of = {"index", "identifier", "name"})
public class StudentClass {

    int index;
    int identifier;
    String name;
    int numberOfStudents;

    Map<TeacherInput, Pair<Integer, Integer>> assignedTeachers = new HashMap<>();

    public StudentClass(int identifier, String name, int numberOfStudents) {
        this.identifier = identifier;
        this.name = name;
        this.numberOfStudents = numberOfStudents;
    }

    public void addTeacher(TeacherInput teacherInput, int hours, int lessons) {
        assignedTeachers.put(teacherInput, new Pair<>(hours, lessons));
    }
}
