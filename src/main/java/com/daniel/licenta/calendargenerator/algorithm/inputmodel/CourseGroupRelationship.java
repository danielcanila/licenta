package com.daniel.licenta.calendargenerator.algorithm.inputmodel;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CourseGroupRelationship {

    private StudentClass course;
    List<StudentClass> studentGroups = new ArrayList<>();

    public CourseGroupRelationship(StudentClass course) {
        this.course = course;
    }
}
