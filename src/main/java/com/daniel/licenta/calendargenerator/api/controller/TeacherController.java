package com.daniel.licenta.calendargenerator.api.controller;

import com.daniel.licenta.calendargenerator.business.model.Teacher;
import com.daniel.licenta.calendargenerator.business.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping
    public List<Teacher> getTeachers() {
        return teacherService.findAll();
    }

    @PostMapping
    public Teacher addTeacher(@RequestBody Teacher teacher) {
        return teacherService.save(teacher);
    }
    @PostMapping("/bulk")
    public List<Teacher> addTeacher(@RequestBody List<Teacher> teachers) {
        return teacherService.saveAll(teachers);
    }

    @DeleteMapping("{id}")
    public String deleteTeacher(@PathVariable("id") Long id) {
        teacherService.delete(id);
        return "Item deleted.";
    }

    @PatchMapping("{id}")
    public Teacher updateTeacher(@PathVariable("id") Long id, @RequestBody Teacher teacher) {
        return teacherService.update(id, teacher);
    }

    @PostMapping("{id}/lecture")
    public Teacher addLecturesToTeacher(@PathVariable("id") Long id, @RequestBody List<Long> ids) {
        return teacherService.addLecturesToTeacher(id, ids);
    }

    @DeleteMapping("{id}/lecture")
    public Teacher deleteLecturesFromTeacher(@PathVariable("id") Long id, @RequestBody List<Long> ids) {
        return teacherService.deleteLecturesFromTeacher(id, ids);
    }

    @PutMapping("{id}/unavailabilityTimeslots")
    public Teacher setUnavailabilityTimeslots(@PathVariable("id") Long id, @RequestBody List<Long> timeslots) {
        return teacherService.setUnavailabilityTimeslots(id, timeslots);
    }
}
