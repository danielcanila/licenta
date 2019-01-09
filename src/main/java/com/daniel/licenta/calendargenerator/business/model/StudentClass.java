package com.daniel.licenta.calendargenerator.business.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "t_student_class")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@EqualsAndHashCode
public class StudentClass {

    @Id
    @SequenceGenerator(name = "student_class_sequence", sequenceName = "student_class_id_sq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_class_sequence")
    private Long id;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_class_id", nullable = true)
    @JsonIdentityReference(alwaysAsId = true)
    private StudentClass studentClass;

    @OneToMany(mappedBy = "studentClass")
    @JsonIdentityReference(alwaysAsId = true)
    private List<StudentClass> studentClasses;

    @ManyToMany(mappedBy = "studentClasses")
    @JsonIdentityReference(alwaysAsId = true)
    private List<Course> courses;


    public boolean hasChildren() {
        return !studentClasses.isEmpty();
    }
}
