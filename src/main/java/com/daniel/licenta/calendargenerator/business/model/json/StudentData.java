package com.daniel.licenta.calendargenerator.business.model.json;

import com.daniel.licenta.calendargenerator.business.model.Teacher;
import javafx.util.Pair;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class StudentData implements Serializable {

    Long identifier;
    String name;
    Long numberOfStudents;

    //    Map<TeacherData, Integer> assignedTeachers = new HashMap<>();
    Set<Pair<Teacher, Integer>> assignedTeachers = new HashSet<>();

}
