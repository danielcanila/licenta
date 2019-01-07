package com.daniel.licenta.calendargenerator.algorithm.outputmodel;

import lombok.Getter;

@Getter
public class RoomOutput {

    int identifier;
    public String name;
    public int capacity;

    public RoomOutput(int identifier, String name, int capacity) {
        this.identifier = identifier;
        this.name = name;
        this.capacity = capacity;
    }


}
