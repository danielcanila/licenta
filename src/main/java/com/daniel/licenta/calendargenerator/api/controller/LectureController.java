package com.daniel.licenta.calendargenerator.api.controller;

import com.daniel.licenta.calendargenerator.business.model.Lecture;
import com.daniel.licenta.calendargenerator.business.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lecture")
public class LectureController {

    @Autowired
    private LectureService lectureService;

    @GetMapping
    public List<Lecture> getLectures() {
        return lectureService.findAll();
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

}
