package com.daniel.licenta.calendargenerator.business.service;

import com.daniel.licenta.calendargenerator.business.common.GenericMapper;
import com.daniel.licenta.calendargenerator.business.model.Lecture;
import com.daniel.licenta.calendargenerator.integration.repo.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class LectureService {

    @Autowired
    private LectureRepository lectureRepository;

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
}
