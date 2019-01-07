package com.daniel.licenta.calendargenerator.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TimetableDTO {

    private List<Long> courseIds;

    private List<Long> roomIds;

    private Long weekSpan;
}
