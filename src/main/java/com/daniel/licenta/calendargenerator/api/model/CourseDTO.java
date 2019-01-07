package com.daniel.licenta.calendargenerator.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CourseDTO {

    private Long teacherId;

    private Long lectureId;

    private List<Long> classIds;
}
