package com.daniel.licenta.calendargenerator.algorithm.outputmodel;

import javafx.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class StudentClassOutput {

    private int identifier;
    private String name;
    private int numberOfStudents;
    private Map<Integer, List<Pair<Integer, Pair<TeacherOutput, RoomOutput>>>> schedule = new HashMap<>();


    public StudentClassOutput(int identifier, String name, int numberOfStudents) {
        this.identifier = identifier;
        this.name = name;
        this.numberOfStudents = numberOfStudents;
    }

    public void addTeachingSession(TeacherOutput teacherOutput, int dayOfWeek, int sessionOfDay, RoomOutput roomOutput) {
        if (schedule.containsKey(dayOfWeek)) {
            schedule.get(dayOfWeek).add(new Pair<>(sessionOfDay, new Pair<>(teacherOutput, roomOutput)));
        } else {
            List<Pair<Integer, Pair<TeacherOutput, RoomOutput>>> list = new ArrayList<>();
            list.add(new Pair<>(sessionOfDay, new Pair<>(teacherOutput, roomOutput)));
            schedule.put(dayOfWeek, list);
        }

        teacherOutput.addClassForTeaching(this, dayOfWeek, sessionOfDay);

    }

}
