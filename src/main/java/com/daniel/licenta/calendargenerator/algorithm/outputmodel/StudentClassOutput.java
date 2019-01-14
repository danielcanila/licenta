package com.daniel.licenta.calendargenerator.algorithm.outputmodel;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javafx.util.Pair;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class StudentClassOutput {

    private int identifier;
    private String name;
    private int numberOfStudents;
    private Map<Integer, TeacherOutput> schedule = new HashMap<>();


    public StudentClassOutput(int identifier, String name, int numberOfStudents) {
        this.identifier = identifier;
        this.name = name;
        this.numberOfStudents = numberOfStudents;
    }

    public void addTeachingSession(TeacherOutput teacherOutput, int sessionOfWeek) {
        TeacherOutput teacherOutputRoomOutputPair = schedule.get(sessionOfWeek);
        if (teacherOutputRoomOutputPair != null) {
            if (teacherOutput.isFree()) {
                return;
            }
            if (!teacherOutputRoomOutputPair.isFree()) {
                throw new RuntimeException("We have duplicates on class :" + name);
            }
            schedule.remove(sessionOfWeek);
        }

        schedule.put(sessionOfWeek, teacherOutput);


        teacherOutput.addClassForTeaching(this, sessionOfWeek);

    }

}
