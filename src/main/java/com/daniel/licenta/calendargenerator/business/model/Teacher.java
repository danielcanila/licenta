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
@Table(name = "t_teacher")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@EqualsAndHashCode
public class Teacher {

    @Id
    @SequenceGenerator(name = "teacher_sequence", sequenceName = "teacher_id_sq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teacher_sequence")
    private Long id;

    @Column
    private String name;

    @ManyToMany
    @JoinTable(
            name = "t_teacher_lecture_link",
            joinColumns = {@JoinColumn(name = "teacher_id")},
            inverseJoinColumns = {@JoinColumn(name = "lecture_id")}
    )
    private List<Lecture> lectures;

    @OneToMany(mappedBy = "teacher")
    @JsonIgnore
    private List<Course> courses;

    @OneToMany(mappedBy = "teacher")
    @JsonIdentityReference(alwaysAsId = true)
    private List<Constraint> constraints;

}
