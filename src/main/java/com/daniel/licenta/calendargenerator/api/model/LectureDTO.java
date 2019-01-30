package com.daniel.licenta.calendargenerator.api.model;

import com.daniel.licenta.calendargenerator.business.model.Teacher;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class LectureDTO implements Serializable {
    private Long id;

    private String name;

    private List<TeacherDTO> teachers = new ArrayList<>();

}
