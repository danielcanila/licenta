package com.daniel.licenta.calendargenerator.api.controller;

import com.daniel.licenta.calendargenerator.business.model.StudentClass;
import com.daniel.licenta.calendargenerator.business.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studentClass")
public class StudentClassController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<StudentClass> getStudentClasses() {
        return studentService.findAllClasses();
    }

    @PostMapping
    public StudentClass addStudentClass(@RequestBody StudentClass studentClass) {
        return studentService.addStudentClass(studentClass);
    }

    @DeleteMapping("{id}")
    public String deleteStudentClass(@PathVariable("id") Long id) {
        studentService.deleteStudentClass(id);
        return "Item deleted.";
    }

    @PatchMapping("{id}")
    public StudentClass updateStudentClass(@PathVariable("id") Long id, @RequestBody StudentClass studentClass) {
        return studentService.updateStudentClass(id, studentClass);
    }

    @PostMapping("{id}/subclasses")
    public StudentClass addStudentsToStudent(@PathVariable("id") Long id, @RequestBody List<Long> ids) {
        return studentService.addClassesToParent(id, ids);
    }


}
