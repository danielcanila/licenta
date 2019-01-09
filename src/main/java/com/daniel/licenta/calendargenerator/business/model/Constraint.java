package com.daniel.licenta.calendargenerator.business.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "t_constraint")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@EqualsAndHashCode
public class Constraint {

    @Id
    @SequenceGenerator(name = "course_sequence", sequenceName = "course_id_sq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_sequence")
    private Long id;

    @Column
    @Enumerated
    private ConstraintType constraintType;

    @Column
    private Boolean hardConstraint = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = true)
    @JsonIdentityReference(alwaysAsId = true)
    private Teacher teacher;


    @ManyToOne
    @JoinColumn(name = "room_id", nullable = true)
    @JsonIdentityReference(alwaysAsId = true)
    private Room room;
}
