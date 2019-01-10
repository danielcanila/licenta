package com.daniel.licenta.calendargenerator.algorithm.inputmodel;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@EqualsAndHashCode(of = {"index", "identifier", "name"})
public class TeacherInput {

    int index;
    int identifier;
    String name;
    Map<StudentClassInput, Integer> assignedStudentClasses = new HashMap<>();
    Map<Integer, List<Integer>> unavailabilityIntervals = new HashMap<>();

    public TeacherInput(int identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public void addStudentClass(StudentClassInput studentClass, int timeSlots) {
        Integer integer = assignedStudentClasses.get(studentClass);
        if (integer != null) {
            assignedStudentClasses.remove(studentClass);
            assignedStudentClasses.put(studentClass, integer + timeSlots);
        } else {
            assignedStudentClasses.put(studentClass, timeSlots);
        }
        studentClass.addTeacher(this, timeSlots);
    }

    public void addUnavailabilityInterval(int day, int timeSlot) {
        if (unavailabilityIntervals.containsKey(day)) {
            unavailabilityIntervals.get(day).add(timeSlot);
        } else {
            List<Integer> list = new ArrayList<>();
            list.add(timeSlot);
            unavailabilityIntervals.put(day, list);
        }
    }

    public void addUnavailabilityInterval(int day, int... timeSlot) {
        for (int i : timeSlot) {
            addUnavailabilityInterval(day, i);
        }
    }
}
