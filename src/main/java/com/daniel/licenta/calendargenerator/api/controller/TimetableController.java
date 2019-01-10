package com.daniel.licenta.calendargenerator.api.controller;

import com.daniel.licenta.calendargenerator.api.model.SlotReservationDTO;
import com.daniel.licenta.calendargenerator.business.model.TimetableResult;
import com.daniel.licenta.calendargenerator.business.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/timetable")
public class TimetableController {


    @Autowired
    private TimetableService timetableService;

    @GetMapping("/triggerConfig/{id}")
    public TimetableResult generateTimetable(@PathVariable("id") long id) {
        return timetableService.triggerCalendarGenerator(id);
    }

    @GetMapping("{id}")
    public TimetableResult retrieveTimetable(@PathVariable("id") long id) {
        return timetableService.getTimetableResult(id);
    }

    @GetMapping("{id}/class/{classId}")
    public List<SlotReservationDTO> retrieveTimetable(@PathVariable("id") long id, @PathVariable("classId") long classId) {
        return timetableService.getTimetableForClass(id, classId);
    }
}
