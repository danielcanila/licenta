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
    List<StudentClass> studentClasses = new ArrayList<>();
    List<TeacherInput> teachers = new ArrayList<>();
    List<RoomInput> rooms = new ArrayList<>();

    public DataInputRepresentation(int timeslotsPerDay) {
        this.timeslotsPerDay = timeslotsPerDay;
    }

    public StudentClass getStudentClassByIndex(int index) {
        return studentClasses.stream().filter(classInput -> classInput.getIndex() == index).findFirst().get();
    }

    public TeacherInput getTeacherByIndex(int index) {
        return teachers.stream().filter(teacherInput -> teacherInput.getIndex() == index).findFirst().get();
    }

    public RoomInput getRoomByIndex(int index) {
        return rooms.stream().filter(teacherInput -> teacherInput.getRoomIndex() == index).findFirst().get();
    }

    public CourseGroupRelationship getRelationshipByIndex(int index) {
        return relationships.stream()
                .filter(courseGroupRelationship -> courseGroupRelationship.getCourse().getIndex() == index)
                .findFirst()
                .orElseGet(() -> null);
    }

    public void setStudentClasses(List<StudentClass> toAdd) {
        studentClasses.clear();
        studentClasses.addAll(toAdd);
        studentClasses.sort(Comparator.comparing(StudentClass::getIdentifier));
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

    public void setRooms(List<RoomInput> toAdd) {
        rooms.clear();
        rooms.addAll(toAdd);
        rooms.sort(Comparator.comparing(RoomInput::getIdentifier));
        for (int i = 0; i < rooms.size(); i++) {
            rooms.get(i).roomIndex = i;
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
