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
@Table(name = "t_slot_reservation")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@EqualsAndHashCode
public class SlotReservation {

    @Id
    @SequenceGenerator(name = "slot_reservation_sequence", sequenceName = "slot_reservation_id_sq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "slot_reservation_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "result_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private TimetableResult timetableResult;

    @Column
    private long day;

    @Column
    private long slot;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @ManyToOne
    @JoinColumn(name = "student_class_id", nullable = false)
    private StudentClass studentClass;

}
