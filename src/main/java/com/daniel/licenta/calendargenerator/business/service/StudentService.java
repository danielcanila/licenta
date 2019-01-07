package com.daniel.licenta.calendargenerator.business.service;

import com.daniel.licenta.calendargenerator.business.common.GenericMapper;
import com.daniel.licenta.calendargenerator.business.model.Student;
import com.daniel.licenta.calendargenerator.business.model.StudentClass;
import com.daniel.licenta.calendargenerator.integration.repo.StudentClassRepository;
import com.daniel.licenta.calendargenerator.integration.repo.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentClassRepository studentClassRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GenericMapper<StudentClass, StudentClass> genericStudentClassMapper;

    @Autowired
    private GenericMapper<Student, Student> genericStudentMapper;

    public List<StudentClass> findAllClasses() {
        return studentClassRepository.findAll();
    }

    public StudentClass addStudentClass(StudentClass studentClass) {
        return studentClassRepository.save(studentClass);
    }

    public void deleteStudentClass(Long id) {
        studentClassRepository.deleteById(id);
    }

    public StudentClass addStudentsToClass(Long id, List<Long> ids) {
        StudentClass studentClass = studentClassRepository.findById(id).orElseThrow(() -> new RuntimeException("Student class not found!"));
        List<Student> studentsToAdd = studentRepository.findAllById(ids);

        studentsToAdd.forEach(student -> {
            studentClass.getStudents().remove(student);

            studentClass.getStudents().add(student);
            student.setStudentClass(studentClass);
        });

        return studentClassRepository.save(studentClass);

    }

    public StudentClass removeStudentsFromClass(Long id, List<Long> ids) {
        StudentClass studentClass = studentClassRepository.findById(id).orElseThrow(() -> new RuntimeException("Student class not found!"));

        List<Student> toRemove = studentClass.getStudents().stream()
                .filter(student -> ids.contains(student.getId()))
                .peek(student -> student.setStudentClass(null))
                .collect(Collectors.toList());

        studentClass.getStudents().removeAll(toRemove);

        return studentClassRepository.save(studentClass);
    }

    public StudentClass updateStudentClass(Long id, StudentClass studentClass) {
        StudentClass toSave = studentClassRepository.findById(id).orElseThrow(() -> new RuntimeException("Student class not found!"));

        genericStudentClassMapper.map(studentClass, toSave);

        return studentClassRepository.save(toSave);

    }

    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public Student updateStudent(Long id, Student student) {
        Student toSave = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found!"));

        genericStudentMapper.map(student, toSave);

        return studentRepository.save(toSave);
    }
}
