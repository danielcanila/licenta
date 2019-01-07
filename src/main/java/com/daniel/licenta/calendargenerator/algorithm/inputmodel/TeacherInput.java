package com.daniel.licenta.calendargenerator.algorithm.inputmodel;

import javafx.util.Pair;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;

@Getter
@EqualsAndHashCode(of = {"index", "identifier", "name"})
public class TeacherInput {

    int index;
    int identifier;
    String name;
    Map<StudentClass, Pair<Integer, Integer>> assignedStudentClasses = new HashMap<>();
    Map<Integer, List<Integer>> unavailabilityIntervals = new HashMap<>();

    public TeacherInput(int identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public void addStudentClass(StudentClass studentClass, int timeSlots, int lessons) {
        assignedStudentClasses.put(studentClass, new Pair<>(timeSlots, lessons));
        studentClass.addTeacher(this, timeSlots, lessons);
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
