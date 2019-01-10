package com.daniel.licenta.calendargenerator.business.model.json;

import com.daniel.licenta.calendargenerator.algorithm.inputmodel.ConfigData;
import com.daniel.licenta.calendargenerator.api.model.RoomDTO;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
public class TimeableConfigData implements Serializable {

    private List<SemiyearRelationship> relationships = new ArrayList<>();
    @JsonIdentityReference(alwaysAsId = true)
    private List<StudentData> studentClasses = new ArrayList<>();
    @JsonIdentityReference(alwaysAsId = true)
    private List<TeacherData> teachers = new ArrayList<>();

    // ROOM id/size
    private List<RoomDTO> rooms = new ArrayList<>();

    private ConfigData configData;

    public void addAssignment(Long teacherId, Long studentDataId, Long numberOfSessions, Long lectureId) {
        StudentData studentData = retrieveStudentDataById(studentDataId);
        TeacherData teacherData = retrieveTeacherDataById(teacherId);
        teacherData.addStudentClass(studentData, numberOfSessions.intValue(), lectureId);
    }

    public void addTeacherIfNotPresent(TeacherData teacherData) {
        boolean found = teachers.stream()
                .anyMatch(td -> td.getIdentifier().equals(teacherData.getIdentifier()));
        if (!found) {
            teachers.add(teacherData);
        }
    }

    public Long determineTeacherWithLowestAmountOfHours(List<Long> teacherIds) {
        for (Long teacherId : teacherIds) {
            if (retrieveTeacherDataById(teacherId) == null) {
                return teacherId;
            }
        }

        return teacherIds
                .stream()
                .map(this::retrieveTeacherDataById)
                .min(Comparator.comparing(TeacherData::retrieveNumberOfAssignedHours))
                .orElse(null)
                .getIdentifier();
    }

    public long retrieveLectureIdForAssignment(long teacherId, long classId) {
        return retrieveTeacherDataById(teacherId)
                .getAssignedStudentClasses()
                .stream()
                .filter(classAssignment -> classAssignment.getClassId().equals(classId))
                .map(ClassAssignment::getLecture)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No assignmnet found for " + teacherId + " - " + classId));
    }

    private StudentData retrieveStudentDataById(Long id) {
        return studentClasses
                .stream()
                .filter(studentData -> studentData.getIdentifier().equals(id))
                .findFirst()
                .orElse(null);
    }

    private TeacherData retrieveTeacherDataById(Long id) {
        return teachers
                .stream()
                .filter(studentData -> studentData.getIdentifier().equals(id))
                .findFirst()
                .orElse(null);
    }

    public long retrieveParentclassById(Long id) {
        return relationships.stream()
                .filter(semiyearRelationship -> semiyearRelationship.getClassIds().contains(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("semiyear not found"))
                .getClassId();
    }
}
