package com.daniel.licenta.calendargenerator.algorithm.core;

import org.springframework.stereotype.Component;

@Component
public class ConfigCSO {

    public int HOURS_IN_WEEK;
    public int HOURS_PER_DAY;


    public void setHOURS_PER_DAY(int HOURS_PER_DAY) {
        this.HOURS_PER_DAY = HOURS_PER_DAY;
        this.HOURS_IN_WEEK = HOURS_PER_DAY * 5;
    }
}
