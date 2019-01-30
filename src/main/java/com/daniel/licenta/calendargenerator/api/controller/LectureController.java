package com.daniel.licenta.calendargenerator.api.controller;

import com.daniel.licenta.calendargenerator.api.model.LectureDTO;
import com.daniel.licenta.calendargenerator.api.model.TeacherDTO;
import com.daniel.licenta.calendargenerator.business.model.Lecture;
import com.daniel.licenta.calendargenerator.business.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lecture")
public class LectureController {

    @Autowired
    private LectureService lectureService;

    @GetMapping
    public List<LectureDTO> getLectures() {
        return lectureService.findAll()
                .stream()
                .map(lecture -> {
                    LectureDTO lectureDTO = new LectureDTO();
                    lectureDTO.setId(lecture.getId());
                    lectureDTO.setName(lecture.getName());
                    lecture.getTeachers().stream()
                            .map(teacher -> new TeacherDTO(teacher.getId(), teacher.getName()))
                            .collect(Collectors.toCollection(lectureDTO::getTeachers));

                    return lectureDTO;
                })
                .collect(Collectors.toList());
    }

    @PostMapping
    public Lecture addLecture(@RequestBody Lecture lecture) {
        return lectureService.save(lecture);
    }

    @DeleteMapping("{id}")
    public String deleteLecture(@PathVariable("id") Long id) {
        lectureService.delete(id);
        return "Item deleted.";
    }

    @PatchMapping("{id}")
    public Lecture updateLecture(@PathVariable("id") Long id, @RequestBody Lecture lecture) {
        return lectureService.update(id, lecture);
    }

    @PostMapping("{id}/teacher")
    public Lecture addTeacherToLecture(@PathVariable("id") Long id, @RequestBody List<Long> teacherIds) {
        return lectureService.addTeachersToLecture(id, teacherIds);
    }

    @DeleteMapping("{id}/teacher")
    public Lecture removeTeacherToLecture(@PathVariable("id") Long id, @RequestBody List<Long> teacherIds) {
        return lectureService.removeTeachersToLecture(id, teacherIds);
    }
}
