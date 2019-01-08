package com.daniel.licenta.calendargenerator.api.controller;

import com.daniel.licenta.calendargenerator.algorithm.inputmodel.ConfigData;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.CalendarOutput;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.RoomOutput;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.StudentClassOutput;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.TeacherOutput;
import com.daniel.licenta.calendargenerator.business.model.Teacher;
import com.daniel.licenta.calendargenerator.business.service.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/timetable")
public class TimetableController {

    @Autowired
    private TimetableService timetableService;

    @PostMapping
    public CalendarOutput generateTimetable(@RequestBody ConfigData configData) {
        return timetableService.generateTimetable(configData);
//        CalendarOutput calendarOutput = new CalendarOutput(1);
//        StudentClassOutput student = new StudentClassOutput(1, "student name", 30);
//        student.addTeachingSession(new TeacherOutput(1, "teacher name"), 0, new RoomOutput(1, "room name", 30));
//        calendarOutput.getStudentClassOutputs().add(student);
//        return calendarOutput;
    }

    @GetMapping
    public CalendarOutput generateTimetable() {
        ConfigData configData = new ConfigData();
        return timetableService.generateTimetable(configData);
    }
}
