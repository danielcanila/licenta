package com.daniel.licenta.calendargenerator.business.model.json;

import com.daniel.licenta.calendargenerator.algorithm.inputmodel.StudentClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherData {

    int identifier;
    String name;
    Map<StudentClass, Integer> assignedStudentClasses = new HashMap<>();
    Map<Integer, List<Integer>> unavailabilityIntervals = new HashMap<>();

}
