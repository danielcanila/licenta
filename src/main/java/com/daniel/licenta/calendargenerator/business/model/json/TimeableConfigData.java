package com.daniel.licenta.calendargenerator.business.model.json;

import com.daniel.licenta.calendargenerator.algorithm.inputmodel.ConfigData;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class TimeableConfigData implements Serializable {

    private List<SemiyearRelationship> relationships = new ArrayList<>();
    private List<StudentData> studentClasses = new ArrayList<>();
    private List<TeacherData> teachers = new ArrayList<>();

    private List<Long> roomIds = new ArrayList<>();

    private ConfigData configData;
}
