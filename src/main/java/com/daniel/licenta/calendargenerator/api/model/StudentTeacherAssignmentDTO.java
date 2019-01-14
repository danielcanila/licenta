package com.daniel.licenta.calendargenerator.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentTeacherAssignmentDTO {

    private Long studentClassId;

    private Long teacherId;

    private Long lectureId;

    private Long numberOfSessions;
}
