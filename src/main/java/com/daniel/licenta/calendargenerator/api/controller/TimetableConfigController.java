package com.daniel.licenta.calendargenerator.api.controller;

import com.daniel.licenta.calendargenerator.algorithm.inputmodel.ConfigData;
import com.daniel.licenta.calendargenerator.api.model.StudentTeacherAssignmentDTO;
import com.daniel.licenta.calendargenerator.business.model.TimetableConfig;
import com.daniel.licenta.calendargenerator.business.service.TimetableConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timetableConfig")
public class TimetableConfigController {

    @Autowired
    private TimetableConfigService timetableConfigService;

    @GetMapping
    public List<TimetableConfig> getTimetableConfigs() {
        return timetableConfigService.retrieveAllTimetableConfigs();
    }

    @GetMapping("/latest")
    public TimetableConfig getTimetableConfigById() {
        return timetableConfigService.retrieveLatestTimetable();
    }

    @PostMapping
    public TimetableConfig createTimetableConfig(@RequestBody ConfigData configData) {
        return timetableConfigService.createTimetable(configData);
    }

    @PatchMapping("{id}")
    public TimetableConfig updateTimetableConfig(@PathVariable("id") long id, @RequestBody ConfigData configData) {
        return timetableConfigService.updateTimetable(id, configData);
    }

    @PostMapping("{id}/studentclasses")
    public TimetableConfig addStudentClasses(@PathVariable("id") long id, @RequestBody List<Long> studentClassIds) {
        return timetableConfigService.addStudentClasses(id, studentClassIds);
    }

    @PostMapping("{id}/assignTeachers")
    public TimetableConfig addTeachersToStudentClasses(@PathVariable("id") long id, @RequestBody List<StudentTeacherAssignmentDTO> assignments) {
        return timetableConfigService.assignTeachersToClasses(id, assignments);
    }

}
