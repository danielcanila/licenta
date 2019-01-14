package com.daniel.licenta.calendargenerator.algorithm.inputmodel;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Setter
@Getter
public class DataInputRepresentation {

    int timeslotsPerDay;
    List<CourseGroupRelationship> relationships = new ArrayList<>();
    List<StudentClassInput> studentClasses = new ArrayList<>();
    List<TeacherInput> teachers = new ArrayList<>();

    public DataInputRepresentation(int timeslotsPerDay) {
        this.timeslotsPerDay = timeslotsPerDay;
    }

    public StudentClassInput getStudentClassByIdentifier(int identifier) {
        return studentClasses.stream().filter(classInput -> classInput.getIdentifier() == identifier).findFirst().get();
    }

    public TeacherInput getTeacherByIndex(int index) {
        return teachers.stream().filter(teacherInput -> teacherInput.getIndex() == index).findFirst().get();
    }

    public void setStudentClasses(List<StudentClassInput> toAdd) {
        studentClasses.clear();
        studentClasses.addAll(toAdd);
        studentClasses.sort(Comparator.comparing(StudentClassInput::getIdentifier));
        for (int i = 0; i < studentClasses.size(); i++) {
            studentClasses.get(i).index = i;
        }
    }

    public void setTeachers(List<TeacherInput> toAdd) {
        teachers.clear();
        teachers.addAll(toAdd);
        teachers.sort(Comparator.comparing(TeacherInput::getIdentifier));
        for (int i = 0; i < teachers.size(); i++) {
            teachers.get(i).index = i;
        }
    }

    public void addTeacher(TeacherInput teacherInput) {
        teacherInput.index = teachers.size();
        teachers.add(teacherInput);
        teachers.sort(Comparator.comparing(TeacherInput::getIdentifier));
    }

    public void setRelationships(List<CourseGroupRelationship> relationships) {
        this.relationships.clear();
        this.relationships.addAll(relationships);
    }
}
