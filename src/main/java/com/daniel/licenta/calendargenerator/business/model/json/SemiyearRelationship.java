package com.daniel.licenta.calendargenerator.business.model.json;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class SemiyearRelationship implements Serializable {

    private Long classId;

    private List<Long> classIds = new ArrayList<>();

    public SemiyearRelationship(Long classId) {
        this.classId = classId;
    }
}
