package com.daniel.licenta.calendargenerator.api.controller;

import com.daniel.licenta.calendargenerator.api.model.SlotReservationDTO;
import com.daniel.licenta.calendargenerator.business.model.TimetableResult;
import com.daniel.licenta.calendargenerator.business.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/timetable")
public class TimetableController {


    @Autowired
    private TimetableService timetableService;

    @GetMapping("/triggerConfig/{id}")
    public String generateTimetable(@PathVariable("id") long id) {
        timetableService.triggerCalendarGenerator(id);
        return "Request was sent. Timetable is generating and should be available soon!";
    }

    @GetMapping()
    public List<TimetableResult> retrieveTimetable() {
        return timetableService.getTimetableResults();
    }

    //    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("{id}/class/{classId}")
    public Map<Long, List<SlotReservationDTO>> retrieveTimetable(@PathVariable("id") long id, @PathVariable("classId") long classId) {
        return timetableService.getTimetableForClass(id, classId);
    }
}
