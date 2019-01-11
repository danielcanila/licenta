package com.daniel.licenta.calendargenerator.api.controller;

import com.daniel.licenta.calendargenerator.api.model.DataDTO;
import com.daniel.licenta.calendargenerator.api.model.SlotReservationDTO;
import com.daniel.licenta.calendargenerator.business.model.TimetableResult;
import com.daniel.licenta.calendargenerator.business.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("{id}/class/{classId}")
    public Map<Long, List<SlotReservationDTO>> retrieveTimetableForClass(@PathVariable("id") long id, @PathVariable("classId") long classId) {
        return timetableService.getTimetableForClass(id, classId);
    }

    @GetMapping("{id}/teacher/{teacherId}")
    public Map<Long, List<SlotReservationDTO>> retrieveTimetableForTeacher(@PathVariable("id") long id, @PathVariable("teacherId") long teacherId) {
        return timetableService.getTimetableForTeacher(id, teacherId);
    }

    @GetMapping("{id}/room/{roomId}")
    public Map<Long, List<SlotReservationDTO>> retrieveTimetableForRoom(@PathVariable("id") long id, @PathVariable("roomId") long roomId) {
        return timetableService.getTimetableForRoom(id, roomId);
    }

    @GetMapping
    public List<DataDTO> retrieveAllTimetables() {
        return timetableService.retrieveAllTimetables();
    }

    @GetMapping("{id}/teacher")
    public List<DataDTO> retrieveAllTeachersFromTimetable(@PathVariable("id") long id) {
        return timetableService.retrieveAllTeachersForTimetable(id);
    }

    @GetMapping("{id}/class")
    public List<DataDTO> retrieveAllClassesFromTimetable(@PathVariable("id") long id) {
        return timetableService.retrieveAllStudentClassesForTimetable(id);
    }

    @GetMapping("{id}/room")
    public List<DataDTO> retrieveAllRoomsFromTimetable(@PathVariable("id") long id) {
        return timetableService.retrieveAllRoomsForTimetable(id);
    }
}
