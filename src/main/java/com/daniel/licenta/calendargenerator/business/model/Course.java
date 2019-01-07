package com.daniel.licenta.calendargenerator.business.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "t_course")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@EqualsAndHashCode
public class Course {

    @Id
    @SequenceGenerator(name = "course_sequence", sequenceName = "course_id_sq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "lecture_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Lecture lecture;

    @ManyToMany
    @JoinTable(
            name = "t_course_student_class_link",
            joinColumns = {@JoinColumn(name = "course_id")},
            inverseJoinColumns = {@JoinColumn(name = "student_class_id")}
    )
    @JsonIdentityReference(alwaysAsId = true)
    private List<StudentClass> studentClasses;

    @OneToMany(mappedBy = "course")
    @JsonIdentityReference(alwaysAsId = true)
    private List<RoomReservation> roomReservations;

    @OneToMany(mappedBy = "course")
    @JsonIdentityReference(alwaysAsId = true)
    private List<Constraint> constraints;
}
