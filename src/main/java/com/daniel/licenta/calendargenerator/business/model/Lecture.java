package com.daniel.licenta.calendargenerator.business.model;

import com.fasterxml.jackson.annotation.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "t_lecture")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@EqualsAndHashCode
public class Lecture {

    @Id
    @SequenceGenerator(name = "lecture_sequence", sequenceName = "lecture_id_sq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lecture_sequence")
    private Long id;

    @Column
    private String name;

    @ManyToMany(mappedBy = "lectures")
    @JsonIgnore
    private List<Teacher> teachers;
}
