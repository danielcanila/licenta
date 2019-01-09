package com.daniel.licenta.calendargenerator.business.model;

import com.daniel.licenta.calendargenerator.business.model.json.TimeableConfigData;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "t_timetable_config")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@EqualsAndHashCode
@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class),
})
public class TimetableConfig implements Serializable {

    @Id
    @SequenceGenerator(name = "timetable_config_sequence", sequenceName = "timetable_config_sq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timetable_config_sequence")
    private Long id;

    @Column
    private String name;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private TimeableConfigData config;
}
