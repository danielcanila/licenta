package com.daniel.licenta.calendargenerator.algorithm.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseGroupRelationshipRecord {
    private int semianIndex;
    private List<Integer> studentGroup = new ArrayList<>();

    public CourseGroupRelationshipRecord(int studentGroupIndex) {
        this.semianIndex = studentGroupIndex;
    }
}
