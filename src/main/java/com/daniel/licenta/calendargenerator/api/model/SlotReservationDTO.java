package com.daniel.licenta.calendargenerator.api.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SlotReservationDTO implements Serializable {

    private long day;

    private long slot;

    private String teacherName;

    private String lectureName;

    private String studentClassName;

    private String roomName;

    private long roomCapacity;

}
