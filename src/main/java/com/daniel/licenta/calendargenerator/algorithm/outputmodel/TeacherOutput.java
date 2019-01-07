package com.daniel.licenta.calendargenerator.algorithm.outputmodel;

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
    private Map<Integer, List<Pair<Integer, StudentClassOutput>>> schedule = new HashMap<>();

    public TeacherOutput(int identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public void addClassForTeaching(StudentClassOutput classOutput, int dayOfWeek, int sessionOfDay) {
        if (schedule.containsKey(dayOfWeek)) {
            schedule.get(dayOfWeek).add(new Pair<>(sessionOfDay, classOutput));
        } else {
            List<Pair<Integer, StudentClassOutput>> list = new ArrayList<>();
            list.add(new Pair<>(sessionOfDay, classOutput));
            schedule.put(dayOfWeek, list);
        }
    }

    public boolean isFree() {
        return EMPTY_TIME_SLOT.equals(name);
    }
}
