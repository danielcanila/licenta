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
@Table(name = "t_room")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@EqualsAndHashCode
public class Room {

    @Id
    @SequenceGenerator(name = "room_sequence", sequenceName = "room_id_sq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_sequence")
    private Long id;

    @Column
    private String name;

    @Column
    private Long capacity;
}
