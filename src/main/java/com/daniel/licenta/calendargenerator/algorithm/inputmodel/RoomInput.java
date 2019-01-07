package com.daniel.licenta.calendargenerator.algorithm.inputmodel;

import lombok.Getter;

@Getter
public class RoomInput {

    public int roomIndex;
    int identifier;
    public String name;
    public int capacity;

    public RoomInput(int identifier, String name, int capacity) {
        this.identifier = identifier;
        this.name = name;
        this.capacity = capacity;
    }
}
