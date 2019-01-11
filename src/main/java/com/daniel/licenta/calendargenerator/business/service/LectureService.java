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
import java.util.Optional;

@Service
@Transactional
public class LectureService {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private GenericMapper<Lecture, Lecture> genericMapper;

    public Lecture save(Lecture lecture) {
        return lectureRepository.save(lecture);
    }

    public List<Lecture> findAll() {
        return lectureRepository.findAll();
    }

    public void delete(Long id) {
        lectureRepository.deleteById(id);
    }

    public Lecture update(Long id, Lecture lecture) {
        Lecture toSave = lectureRepository.findById(id).orElseThrow(() -> new RuntimeException("Lecture not found!"));

        genericMapper.map(lecture, toSave);
        lectureRepository.save(toSave);

        return toSave;
    }

    public Lecture addTeachersToLecture(Long id, List<Long> teacherIds) {
        Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new RuntimeException("Lecture not found!"));

        List<Teacher> teachers = teacherRepository.findAllById(teacherIds);

        lecture.getTeachers().removeAll(teachers);
        lecture.getTeachers().addAll(teachers);
        teachers.forEach(teacher -> teacher.getLectures().remove(lecture));
        teachers.forEach(teacher -> teacher.getLectures().add(lecture));

        teacherRepository.saveAll(teachers);
        return lectureRepository.save(lecture);
    }

    public Lecture removeTeachersToLecture(Long id, List<Long> teacherIds) {
        Lecture lecture = lectureRepository.findById(id).orElseThrow(() -> new RuntimeException("Lecture not found!"));

        List<Teacher> teachers = teacherRepository.findAllById(teacherIds);

        lecture.getTeachers().removeAll(teachers);
        teachers.forEach(teacher -> teacher.getLectures().remove(lecture));

        teacherRepository.saveAll(teachers);
        return lectureRepository.save(lecture);
    }
}
