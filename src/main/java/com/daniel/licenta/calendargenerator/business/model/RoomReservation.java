package com.daniel.licenta.calendargenerator.business.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "t_room_reservation")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class RoomReservation {

    @Id
    @SequenceGenerator(name = "room_reservation_sequence", sequenceName = "room_reservation_id_sq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_reservation_sequence")
    private Long id;

    @Column
    private LocalDateTime localDateTime;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Course course;


}
