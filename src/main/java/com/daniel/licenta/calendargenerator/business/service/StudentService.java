package com.daniel.licenta.calendargenerator.business.service;

import com.daniel.licenta.calendargenerator.business.common.GenericMapper;
import com.daniel.licenta.calendargenerator.business.model.StudentClass;
import com.daniel.licenta.calendargenerator.integration.repo.StudentClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentClassRepository studentClassRepository;

    @Autowired
    private GenericMapper<StudentClass, StudentClass> genericStudentClassMapper;

    public List<StudentClass> findAllClasses() {
        return studentClassRepository.findAll();
    }

    public StudentClass addStudentClass(StudentClass studentClass) {
        return studentClassRepository.save(studentClass);
    }

    public StudentClass addClassesToParent(Long id, List<Long> ids) {
        StudentClass toSave = studentClassRepository.findById(id).orElseThrow(() -> new RuntimeException("Student class not found!"));

        List<StudentClass> allById = studentClassRepository.findAllById(ids);
        toSave.getStudentClasses().addAll(allById);
        allById.forEach(studentClass -> studentClass.setStudentClass(toSave));

        return studentClassRepository.save(toSave);
    }

    public void deleteStudentClass(Long id) {
        studentClassRepository.deleteById(id);
    }

    public StudentClass updateStudentClass(Long id, StudentClass studentClass) {
        StudentClass toSave = studentClassRepository.findById(id).orElseThrow(() -> new RuntimeException("Student class not found!"));

        genericStudentClassMapper.map(studentClass, toSave);

        return studentClassRepository.save(toSave);

    }


    public List<StudentClass> addStudentClasses(List<StudentClass> studentClasses) {
        return studentClassRepository.saveAll(studentClasses);
    }
}
