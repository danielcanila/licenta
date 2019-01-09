package com.daniel.licenta.calendargenerator.business.model.json;

import com.daniel.licenta.calendargenerator.algorithm.inputmodel.TeacherInput;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class StudentData {

    Long identifier;
    String name;
    Long numberOfStudents;

    Map<TeacherData, Integer> assignedTeachers = new HashMap<>();

    public StudentData(Long identifier, String name, Long numberOfStudents) {
        this.identifier = identifier;
        this.name = name;
        this.numberOfStudents = numberOfStudents;
    }
}
