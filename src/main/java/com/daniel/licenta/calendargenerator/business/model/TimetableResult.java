package com.daniel.licenta.calendargenerator.business.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "t_timetable_result")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@EqualsAndHashCode
public class TimetableResult {

    @Id
    @SequenceGenerator(name = "timetable_result_sequence", sequenceName = "timetable_result_id_sq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timetable_result_sequence")
    private Long id;

    @OneToMany(mappedBy = "timetableResult")
    private List<SlotReservation> slotReservations = new ArrayList<>();


    public void addSlotReservations(List<SlotReservation> slotReservations) {
        slotReservations.forEach(slotReservation -> slotReservation.setTimetableResult(this));
        this.slotReservations.addAll(slotReservations);
    }
}
