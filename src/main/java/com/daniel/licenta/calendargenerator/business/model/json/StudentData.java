package com.daniel.licenta.calendargenerator.business.model.json;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"assignedTeachers"})
@NoArgsConstructor
public class StudentData implements Serializable {

    Long identifier;
    String name;
    Long numberOfStudents;

    public StudentData(Long identifier, String name, Long numberOfStudents) {
        this.identifier = identifier;
        this.name = name;
        this.numberOfStudents = numberOfStudents;
    }

}
