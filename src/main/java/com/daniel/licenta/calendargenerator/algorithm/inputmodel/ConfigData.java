package com.daniel.licenta.calendargenerator.algorithm.inputmodel;

import com.sun.xml.internal.ws.developer.Serialization;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class ConfigData implements Serializable {
    public String name;
    public Long mr;
    public Long cats;
    public Long iterations;
    public Long refinementSteps;
    public Double maxFitness;
    public Long hardConstraintWeight;
    public Long seekingMemoryPool;
    public Long seekingRangeDimension;
    public Long hoursInWeek;
    public Long hoursPerDay;
    public Long seed;
    public Double emptyPeriodWeight;
    public Double teacherDispersionWeight;
    public Double emptyPeriodWeightOptimizationPhase;
    public Double teacherDispersionWeightOptimizationPhase;
    public Long timeslotsPerDay;

}
