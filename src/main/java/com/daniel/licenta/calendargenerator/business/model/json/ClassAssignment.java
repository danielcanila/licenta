package com.daniel.licenta.calendargenerator.business.model.json;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ClassAssignment implements Serializable {

    private Long classId;

    private int numberOfHours;

    private long lecture;
}
