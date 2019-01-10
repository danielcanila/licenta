package com.daniel.licenta.calendargenerator.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class RoomDTO implements Serializable {

    private Long roomId;
    private Long roomCapacity;
    private String name;

    public RoomDTO(Long roomId, Long roomCapacity, String name) {
        this.roomId = roomId;
        this.roomCapacity = roomCapacity;
        this.name = name;
    }
}
