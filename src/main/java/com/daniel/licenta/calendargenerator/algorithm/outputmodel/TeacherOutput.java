package com.daniel.licenta.calendargenerator.algorithm.outputmodel;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javafx.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.daniel.licenta.calendargenerator.algorithm.util.CSOConstants.EMPTY_TIME_SLOT;

@Getter
public class TeacherOutput {

    private int identifier;
    private String name;
    @JsonBackReference
    private Map<Integer, List<StudentClassOutput>> schedule = new HashMap<>();

    public TeacherOutput(int identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public void addClassForTeaching(StudentClassOutput classOutput, int sessionOfWeek) {
        if (schedule.containsKey(sessionOfWeek)) {
            schedule.get(sessionOfWeek).add(classOutput);
        } else {
            List<StudentClassOutput> list = new ArrayList<>();
            list.add(classOutput);
            schedule.put(sessionOfWeek, list);
        }
    }

    public boolean isFree() {
        return EMPTY_TIME_SLOT.equals(name);
    }
}
