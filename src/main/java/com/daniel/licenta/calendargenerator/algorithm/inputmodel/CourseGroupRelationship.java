package com.daniel.licenta.calendargenerator.algorithm.inputmodel;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CourseGroupRelationship {

    private StudentClassInput course;
    List<StudentClassInput> studentGroups = new ArrayList<>();

    public CourseGroupRelationship(StudentClassInput course) {
        this.course = course;
    }
}
