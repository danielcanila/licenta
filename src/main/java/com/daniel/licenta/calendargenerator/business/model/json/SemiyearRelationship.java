package com.daniel.licenta.calendargenerator.business.model.json;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SemiyearRelationship {

    private Long classId;

    private List<Long> classIds = new ArrayList<>();

    public SemiyearRelationship(Long classId) {
        this.classId = classId;
    }
}
