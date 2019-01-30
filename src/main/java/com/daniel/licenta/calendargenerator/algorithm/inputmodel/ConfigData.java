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
    public Long mr = 4L;
    public Long cats = 30L;
    public Long iterations = 500L;
    public Long refinementSteps = 10000L;
    public Double maxFitness;
    public Long hardConstraintWeight;
    public Long seekingMemoryPool;
    public Long seekingRangeDimension;
    public Long hoursInWeek = 30L;
    public Long hoursPerDay = 6L;
    public Long seed;
    public Double emptyPeriodWeight;
    public Double teacherDispersionWeight;
    public Double emptyPeriodWeightOptimizationPhase;
    public Double teacherDispersionWeightOptimizationPhase;
    public Long timeslotsPerDay = 6L;

}
