package com.daniel.licenta.calendargenerator.business.service;

import com.daniel.licenta.calendargenerator.api.model.CourseDTO;
import com.daniel.licenta.calendargenerator.business.model.Course;
import com.daniel.licenta.calendargenerator.business.model.Lecture;
import com.daniel.licenta.calendargenerator.business.model.StudentClass;
import com.daniel.licenta.calendargenerator.business.model.Teacher;
import com.daniel.licenta.calendargenerator.integration.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private StudentClassRepository studentClassRepository;

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Course addCourse(CourseDTO courseDTO) {
        Teacher teacher = teacherRepository.findById(courseDTO.getTeacherId()).orElseThrow(() -> new RuntimeException("Teacher not found!"));
        Lecture lecture = lectureRepository.findById(courseDTO.getLectureId()).orElseThrow(() -> new RuntimeException("Lecture not found!"));
        List<StudentClass> studentClasses = studentClassRepository.findAllById(courseDTO.getClassIds());

        Course course = new Course();
        course.setTeacher(teacher);
        course.setLecture(lecture);
        course.setStudentClasses(studentClasses);

        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
