package com.daniel.licenta.calendargenerator.business.model;

import com.fasterxml.jackson.annotation.*;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "t_teacher")
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@EqualsAndHashCode
@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class),
})
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

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @JsonManagedReference
    private List<Long> unavailabilitySlots = new ArrayList<>();
}
