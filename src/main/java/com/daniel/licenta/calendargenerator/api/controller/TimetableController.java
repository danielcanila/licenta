package com.daniel.licenta.calendargenerator.api.controller;

import com.daniel.licenta.calendargenerator.api.model.TimetableDTO;
import com.daniel.licenta.calendargenerator.business.model.RoomReservation;
import com.daniel.licenta.calendargenerator.business.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/timetable")
public class TimetableController {

    @Autowired
    private TimetableService timetableService;

    @PostMapping
    public List<RoomReservation> generateTimetable(TimetableDTO timeTableDTO) {
        return timetableService.generateTimetable(timeTableDTO);
    }
}
