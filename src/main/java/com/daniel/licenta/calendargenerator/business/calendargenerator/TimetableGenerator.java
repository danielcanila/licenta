package com.daniel.licenta.calendargenerator.business.calendargenerator;

import com.daniel.licenta.calendargenerator.algorithm.TimeTableGeneratorCSO;
import com.daniel.licenta.calendargenerator.algorithm.inputmodel.*;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.CalendarOutput;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.RoomOutput;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.TeacherOutput;
import com.daniel.licenta.calendargenerator.business.model.*;
import com.daniel.licenta.calendargenerator.business.model.json.TimeableConfigData;
import com.daniel.licenta.calendargenerator.integration.repo.LectureRepository;
import com.daniel.licenta.calendargenerator.integration.repo.RoomRepository;
import com.daniel.licenta.calendargenerator.integration.repo.StudentClassRepository;
import com.daniel.licenta.calendargenerator.integration.repo.TeacherRepository;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TimetableGenerator {

    @Autowired
    private TimeTableGeneratorCSO timeTableGeneratorCSO;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private StudentClassRepository studentClassRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private LectureRepository lectureRepository;

    public TimetableResult generateTimeTable(TimeableConfigData config) {

        DataInputRepresentation dataInputRepresentation = mapDataToInput(config);
        CalendarOutput calendarOutput = timeTableGeneratorCSO.generateTimetable(dataInputRepresentation, config.getConfigData());
        TimetableResult timetableResult = mapDataToResult(calendarOutput, config);

        return timetableResult;
    }

    private TimetableResult mapDataToResult(CalendarOutput calendarOutput, TimeableConfigData config) {
        TimetableResult timetableResult = new TimetableResult();
        int slotsPerDay = calendarOutput.getSlotsPerDay();
        List<SlotReservation> reservations = calendarOutput
                .getStudentClassOutputs()
                .stream()
                .flatMap(studClass -> studClass
                        .getSchedule()
                        .entrySet()
                        .stream()
                        .filter(entry -> !entry.getValue().getKey().isFree())
                        .map(entry -> {
                            Integer slot = entry.getKey();
                            Pair<TeacherOutput, RoomOutput> result = entry.getValue();

                            Teacher teacher = teacherRepository.findById((long) result.getKey().getIdentifier())
                                    .orElseThrow(() -> new RuntimeException("A)Not found " + result.getKey().getIdentifier()));
                            StudentClass studentClass = studentClassRepository.findById((long) studClass.getIdentifier())
                                    .orElseThrow(() -> new RuntimeException("B)Not found " + studClass.getIdentifier()));
                            Room room = roomRepository.findById((long) result.getValue().getIdentifier())
                                    .orElseThrow(() -> new RuntimeException("C)Not found " + result.getValue().getIdentifier()));

                            Lecture lecture;
                            if (result.getValue().isCourseClass()) {
                                lecture = lectureRepository.findById(config.retrieveLectureIdForAssignment(teacher.getId(), config.retrieveParentclassById(studentClass.getId())))
                                        .orElseThrow(() -> new RuntimeException("Lecture not found"));
                            } else {
                                lecture = lectureRepository.findById(config.retrieveLectureIdForAssignment(teacher.getId(), studentClass.getId()))
                                        .orElseThrow(() -> new RuntimeException("Lecture not found"));
                            }

                            SlotReservation reservation = new SlotReservation();
                            reservation.setDay(slot / slotsPerDay);
                            reservation.setSlot(slot % slotsPerDay);
                            reservation.setTeacher(teacher);
                            reservation.setStudentClass(studentClass);
                            reservation.setRoom(room);
                            reservation.setLecture(lecture);
                            return reservation;
                        })

                )
                .collect(Collectors.toList());

        timetableResult.addSlotReservations(reservations);
        return timetableResult;
    }

    private DataInputRepresentation mapDataToInput(TimeableConfigData config) {
        DataInputRepresentation dataInputRepresentation = new DataInputRepresentation(config.getConfigData().getTimeslotsPerDay().intValue());

        List<RoomInput> rooms = config.getRooms()
                .stream()
                .map(dto -> new RoomInput(dto.getRoomId().intValue(), dto.getName(), dto.getRoomCapacity().intValue()))
                .collect(Collectors.toList());
        dataInputRepresentation.setRooms(rooms);

        List<StudentClassInput> studentClasses = config.getStudentClasses()
                .stream()
                .map(sclass -> new StudentClassInput(sclass.getIdentifier().intValue(), sclass.getName(), sclass.getNumberOfStudents().intValue()))
                .collect(Collectors.toList());
        dataInputRepresentation.setStudentClasses(studentClasses);

        List<CourseGroupRelationship> relations = config.getRelationships()
                .stream()
                .map(relationship -> {
                    CourseGroupRelationship rel = new CourseGroupRelationship(dataInputRepresentation.getStudentClassByIdentifier(relationship.getClassId().intValue()));
                    relationship.getClassIds()
                            .stream()
                            .map(id -> dataInputRepresentation.getStudentClassByIdentifier(id.intValue()))
                            .collect(Collectors.toCollection(rel::getStudentGroups));
                    return rel;
                })
                .collect(Collectors.toList());
        dataInputRepresentation.setRelationships(relations);

        List<TeacherInput> teachers = config.getTeachers()
                .stream()
                .map(teacherData -> {
                    TeacherInput teacher = new TeacherInput(teacherData.getIdentifier().intValue(), teacherData.getName());
                    teacherData.getAssignedStudentClasses().forEach(classAssignment -> {
                        teacher.addStudentClass(dataInputRepresentation.getStudentClassByIdentifier(classAssignment.getClassId().intValue()), classAssignment.getNumberOfHours());
                    });
                    teacherData.getUnavailabilityIntervals().entrySet()
                            .forEach(entry ->
                                    entry.getValue()
                                            .forEach(hour -> {
                                                        teacher.addUnavailabilityInterval(entry.getKey(), hour);
                                                    }
                                            )
                            );

                    return teacher;
                })
                .collect(Collectors.toList());

        dataInputRepresentation.setTeachers(teachers);
        return dataInputRepresentation;
    }

}
