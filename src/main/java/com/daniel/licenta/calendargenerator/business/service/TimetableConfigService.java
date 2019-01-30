package com.daniel.licenta.calendargenerator.business.service;

import com.daniel.licenta.calendargenerator.algorithm.inputmodel.ConfigData;
import com.daniel.licenta.calendargenerator.api.model.RoomDTO;
import com.daniel.licenta.calendargenerator.api.model.StudentTeacherAssignmentDTO;
import com.daniel.licenta.calendargenerator.business.calendargenerator.TimetableGenerator;
import com.daniel.licenta.calendargenerator.business.common.GenericMapper;
import com.daniel.licenta.calendargenerator.business.model.*;
import com.daniel.licenta.calendargenerator.business.model.json.SemiyearRelationship;
import com.daniel.licenta.calendargenerator.business.model.json.StudentData;
import com.daniel.licenta.calendargenerator.business.model.json.TeacherData;
import com.daniel.licenta.calendargenerator.business.model.json.TimeableConfigData;
import com.daniel.licenta.calendargenerator.integration.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimetableConfigService {

    @Autowired
    private TimetableConfigRepository timetableConfigRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private StudentClassRepository studentClassRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private GenericMapper<ConfigData, ConfigData> genericMapper;

    @Autowired
    private TimetableGenerator timetableGenerator;

    public TimetableConfig createTimetable(ConfigData configData) {
        TimetableConfig timetableConfig = new TimetableConfig();
        timetableConfig.setName(configData.name);

        TimeableConfigData data = new TimeableConfigData();
        data.setConfigData(configData);
        timetableConfig.setConfig(data);

        return timetableConfigRepository.save(timetableConfig);
    }

    public TimetableConfig updateTimetable(long id, ConfigData configData) {
        TimetableConfig timetableConfig = timetableConfigRepository.findById(id).orElseThrow(() -> new RuntimeException("Config not found!"));

        genericMapper.map(configData, timetableConfig.getConfig().getConfigData());
        return timetableConfigRepository.save(timetableConfig);
    }

    public TimetableConfig addStudentClasses(long id, List<Long> studentClassIds) {
        TimetableConfig timetableConfig = timetableConfigRepository.findById(id).orElseThrow(() -> new RuntimeException("Config not found!"));

        List<StudentClass> studentClasses = studentClassRepository.findAllById(studentClassIds);

        studentClasses
                .stream()
                .map(studentClass -> new StudentData(studentClass.getId(), studentClass.getName(), studentClass.hasChildren() ? 100l : 30l))
                .collect(Collectors.toCollection(() -> timetableConfig.getConfig().getStudentClasses()));

        studentClasses.stream()
                .filter(StudentClass::hasChildren)
                .map(studentClass -> {
                    SemiyearRelationship semiyearRelationship = new SemiyearRelationship(studentClass.getId());
                    studentClass.getStudentClasses()
                            .stream()
                            .map(StudentClass::getId)
                            .collect(Collectors.toCollection(() -> semiyearRelationship.getClassIds()));
                    return semiyearRelationship;
                })
                .collect(Collectors.toCollection(() -> timetableConfig.getConfig().getRelationships()));

        return timetableConfigRepository.save(timetableConfig);
    }


    public List<TimetableConfig> retrieveAllTimetableConfigs() {
        List<TimetableConfig> all = timetableConfigRepository.findAll();
        return all;
    }


    public TimetableConfig assignTeachersToClasses(long id, List<StudentTeacherAssignmentDTO> assignments) {
        List<Long> studentsToAdd = assignments.stream()
                .map(StudentTeacherAssignmentDTO::getStudentClassId)
                .distinct()
                .collect(Collectors.toList());


        addStudentClasses(id, studentsToAdd);

        TimetableConfig timetableConfig = timetableConfigRepository.findById(id).orElseThrow(() -> new RuntimeException("Config not found!"));

        handleAssignmentsWithTeachers(assignments, timetableConfig);
        handleAssignmentsWithoutTeachers(assignments, timetableConfig);

        return timetableConfigRepository.save(timetableConfig);
    }

    private void handleAssignmentsWithoutTeachers(List<StudentTeacherAssignmentDTO> assignments, TimetableConfig timetableConfig) {
        List<Long> assignmentsWithLectures = assignments.stream()
                .filter(assignment -> assignment.getTeacherId() == null)
                .map(StudentTeacherAssignmentDTO::getLectureId)
                .collect(Collectors.toList());

        List<Lecture> lectures = lectureRepository.findAllById(assignmentsWithLectures);

        int hoursPerDay = timetableConfig.getConfig().getConfigData().getHoursPerDay().intValue();

        assignments.stream()
                .filter(assignment -> assignment.getTeacherId() == null)
                .forEach(assignmentDTO -> {
                    Lecture lecture = lectures.stream()
                            .filter(lect -> lect.getId().equals(assignmentDTO.getLectureId()))
                            .findFirst()
                            .orElse(null);
                    List<Long> involvedTeacher =
                            lecture.getTeachers()
                                    .stream()
                                    .map(Teacher::getId)
                                    .collect(Collectors.toList());


                    Long teacher = timetableConfig.getConfig().determineTeacherWithLowestAmountOfHours(involvedTeacher);
                    TeacherData teacherData = teacherRepository
                            .findById(teacher)
                            .map(foundTeacher -> mapTeacherToData(hoursPerDay, foundTeacher))
                            .get();
                    timetableConfig
                            .getConfig()
                            .addTeacherIfNotPresent(teacherData);

                    timetableConfig.getConfig().addAssignment(
                            teacherData.getIdentifier(),
                            assignmentDTO.getStudentClassId(),
                            assignmentDTO.getNumberOfSessions(),
                            assignmentDTO.getLectureId()
                    );
                });
    }

    private void handleAssignmentsWithTeachers(List<StudentTeacherAssignmentDTO> assignments, TimetableConfig timetableConfig) {
        List<StudentTeacherAssignmentDTO> assignmentsWithTeachers =
                assignments.stream()
                        .filter(assignment -> assignment.getTeacherId() != null)
                        .collect(Collectors.toList());

        List<Long> teacherIds = assignmentsWithTeachers
                .stream()
                .map(StudentTeacherAssignmentDTO::getTeacherId)
                .distinct()
                .collect(Collectors.toList());

        List<Teacher> teachers = teacherRepository.findAllById(teacherIds);
        int hoursPerDay = timetableConfig.getConfig().getConfigData().getHoursPerDay().intValue();

        teachers.stream()
                .map(teacher -> mapTeacherToData(hoursPerDay, teacher))
                .collect(Collectors.toCollection(() -> timetableConfig.getConfig().getTeachers()));

        if (teacherIds.size() != teachers.size()) {
            throw new RuntimeException("Not all teachers were found!");
        }

        assignmentsWithTeachers.forEach(assignment -> {
            timetableConfig
                    .getConfig()
                    .addAssignment(
                            assignment.getTeacherId(),
                            assignment.getStudentClassId(),
                            assignment.getNumberOfSessions(),
                            assignment.getLectureId());
        });
    }

    private TeacherData mapTeacherToData(int hoursPerDay, Teacher teacher) {
        TeacherData teacherData = new TeacherData();
        teacherData.setIdentifier(teacher.getId());
        teacherData.setName(teacher.getName());
        teacher.getUnavailabilitySlots()
                .forEach(timeslot -> {
                    teacherData.addUnavailabilityInterval(
                            timeslot.intValue() / hoursPerDay,
                            timeslot.intValue() % hoursPerDay);
                });
        return teacherData;
    }

    public TimetableConfig retrieveLatestTimetable() {
        return retrieveAllTimetableConfigs()
                .stream()
                .max(Comparator.comparing(TimetableConfig::getId))
                .get();
    }
}
