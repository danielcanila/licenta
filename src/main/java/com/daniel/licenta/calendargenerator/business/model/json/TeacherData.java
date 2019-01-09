package com.daniel.licenta.calendargenerator.business.model.json;

import javafx.util.Pair;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TeacherData implements Serializable {

    int identifier;
    String name;
    //    Map<StudentData, Integer> assignedStudentClasses = new HashMap<>();
    Set<Pair<StudentData, Integer>> assignedStudentClasses = new HashSet<>();
    Map<Integer, List<Integer>> unavailabilityIntervals = new HashMap<>();

}
