package com.daniel.licenta.calendargenerator.api.model;

import lombok.Data;

@Data
public class StudentTeacherAssignmentDTO {

    private Long studentClassId;

    private Long teacherId;

    private Long lectureId;

    private Long numberOfSessions;
}
