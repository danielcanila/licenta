package com.daniel.licenta.calendargenerator.business.service;

import com.daniel.licenta.calendargenerator.business.common.GenericMapper;
import com.daniel.licenta.calendargenerator.business.model.Lecture;
import com.daniel.licenta.calendargenerator.business.model.Teacher;
import com.daniel.licenta.calendargenerator.integration.repo.LectureRepository;
import com.daniel.licenta.calendargenerator.integration.repo.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private GenericMapper<Teacher, Teacher> genericMapper;

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public void delete(Long teacherId) {
        teacherRepository.deleteById(teacherId);
    }

    public Teacher update(Long teacherId, Teacher teacher) {
        Teacher toSave = teacherRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found!"));

        genericMapper.map(teacher, toSave);
        teacherRepository.save(toSave);

        return toSave;
    }

    public Teacher addLecturesToTeacher(Long teacherId, List<Long> lectureIds) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));
        List<Lecture> lecturesToAdd = lectureRepository.findAllById(lectureIds);

        teacher.getLectures().forEach(lecture ->{
            lecture.getTeachers().remove(teacher);

        });
        teacher.getLectures().clear();

        lecturesToAdd.forEach(lecture -> {
            lecture.getTeachers().remove(teacher);
            teacher.getLectures().remove(lecture);

            lecture.getTeachers().add(teacher);
            teacher.getLectures().add(lecture);
        });

        return teacherRepository.save(teacher);
    }

    public Teacher deleteLecturesFromTeacher(Long teacherId, List<Long> lectureIds) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new RuntimeException("Teacher not found"));

        List<Lecture> toRemoveFrom = teacher.getLectures().stream()
                .filter(lecture -> lectureIds.contains(lecture.getId()))
                .peek(lecture -> lecture.getTeachers().remove(teacher))
                .collect(Collectors.toList());

        teacher.getLectures().removeAll(toRemoveFrom);

        return teacherRepository.save(teacher);
    }

    public Teacher setUnavailabilityTimeslots(Long id, List<Long> unavailabilityTimeSlots) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new RuntimeException("Teacher not found"));
        teacher.setUnavailabilitySlots(unavailabilityTimeSlots);
        return teacherRepository.save(teacher);
    }

    public List<Teacher> saveAll(List<Teacher> teachers) {
        return teacherRepository.saveAll(teachers);
    }
}

