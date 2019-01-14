package com.daniel.licenta.calendargenerator.api.controller;

import com.daniel.licenta.calendargenerator.algorithm.inputmodel.ConfigData;
import com.daniel.licenta.calendargenerator.api.model.StudentTeacherAssignmentDTO;
import com.daniel.licenta.calendargenerator.business.model.TimetableConfig;
import com.daniel.licenta.calendargenerator.business.service.TimetableConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/timetableConfig")
public class TimetableConfigController {

    @Autowired
    private TimetableConfigService timetableConfigService;

    @GetMapping
    public List<TimetableConfig> getTimetableConfigs(){
        return timetableConfigService.retrieveAllTimetableConfigs();
    }

    @PostMapping
    public TimetableConfig createTimetableConfig(@RequestBody ConfigData configData) {
        return timetableConfigService.createTimetable(configData);
    }

    @PatchMapping("{id}")
    public TimetableConfig updateTimetableConfig(@PathVariable("id") long id, @RequestBody ConfigData configData) {
        return timetableConfigService.updateTimetable(id, configData);
    }

    @PostMapping("{id}/room")
    public TimetableConfig addRoomsToTimetableConfig(@PathVariable("id") long id, @RequestBody List<Long> roomIds) {
        return timetableConfigService.addRooms(id, roomIds);
    }

    @PostMapping("{id}/studentclasses")
    public TimetableConfig addStudentClasses(@PathVariable("id") long id, @RequestBody List<Long> studentClassIds) {
        return timetableConfigService.addStudentClasses(id, studentClassIds);
    }

    @PostMapping("{id}/assignTeachers")
    public TimetableConfig addTeachersToStudentClasses(@PathVariable("id") long id, @RequestBody List<StudentTeacherAssignmentDTO> assignments) {
        List<StudentTeacherAssignmentDTO> l = new ArrayList<>();
        for (long i = 1; i < 25; i++) {
            l.add(new StudentTeacherAssignmentDTO(i, null, 1L, 2L));
            l.add(new StudentTeacherAssignmentDTO(i, null, 2L, 2L));
            l.add(new StudentTeacherAssignmentDTO(i, null, 3L, 4L));
            l.add(new StudentTeacherAssignmentDTO(i, null, 4L, 4L));
            l.add(new StudentTeacherAssignmentDTO(i, null, 5L, 3L));
            l.add(new StudentTeacherAssignmentDTO(i, null, 6L, 2L));
            l.add(new StudentTeacherAssignmentDTO(i, null, 7L, 2L));
            l.add(new StudentTeacherAssignmentDTO(i, null, 8L, 2L));
            l.add(new StudentTeacherAssignmentDTO(i, null, 9L, 2L));
            l.add(new StudentTeacherAssignmentDTO(i, null, 10L, 1L));
            l.add(new StudentTeacherAssignmentDTO(i, null, 11L, 2L));
            l.add(new StudentTeacherAssignmentDTO(i, null, 12L, 2L));
            l.add(new StudentTeacherAssignmentDTO(i, null, 13L, 1L));
            l.add(new StudentTeacherAssignmentDTO(i, null, 14L, 1L));
        }

        return timetableConfigService.assignTeachersToClasses(id, l);
    }

}
