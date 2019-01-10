package com.daniel.licenta.calendargenerator.algorithm.inputmodel;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode(of = {"index", "identifier", "name"})
public class StudentClassInput {

    int index;
    int identifier;
    String name;
    int numberOfStudents;

    Map<TeacherInput, Integer> assignedTeachers = new HashMap<>();

    public StudentClassInput(int identifier, String name, int numberOfStudents) {
        this.identifier = identifier;
        this.name = name;
        this.numberOfStudents = numberOfStudents;
    }

    public void addTeacher(TeacherInput teacherInput, int hours) {
        Integer integer = assignedTeachers.get(teacherInput);
        if (integer != null) {
            assignedTeachers.remove(teacherInput);
            assignedTeachers.put(teacherInput, integer + hours);
        } else {
            assignedTeachers.put(teacherInput, hours);
        }
    }
}
