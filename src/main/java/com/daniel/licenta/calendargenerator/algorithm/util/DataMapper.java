package com.daniel.licenta.calendargenerator.algorithm.util;

import com.daniel.licenta.calendargenerator.algorithm.inputmodel.*;
import com.daniel.licenta.calendargenerator.algorithm.model.*;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.CalendarOutput;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.RoomOutput;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.StudentClassOutput;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.TeacherOutput;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.daniel.licenta.calendargenerator.algorithm.util.CSOConstants.EMPTY_TIME_SLOT;

@Component
public class DataMapper {

    public CalendarData mapToCalendarData(DataInputRepresentation dataInputRepresentation) {
        fillDataWithEmptyTimeSlots(dataInputRepresentation);

        StudentGroup[] classRecords = mapClass(dataInputRepresentation);
        Teacher[] teachers = mapTeachers(dataInputRepresentation);
        RoomRecord[] roomRecords = mapRooms(dataInputRepresentation);
        List<CourseGroupRelationshipRecord> courseGroupRelationshipRecords = mapCourseGroupRelationship(dataInputRepresentation);


        CalendarData calendarData = new CalendarData(classRecords.length, teachers.length);
        calendarData.setTeachers(teachers);
        calendarData.setStudentGroups(classRecords);
        calendarData.setRooms(roomRecords);
        calendarData.setNumberOfSlotsPerDay(dataInputRepresentation.getTimeslotsPerDay());
        calendarData.setCourseGroupRelationshipRecords(courseGroupRelationshipRecords);

        return calendarData;
    }

    private List<CourseGroupRelationshipRecord> mapCourseGroupRelationship(DataInputRepresentation dataInputRepresentation) {
        return dataInputRepresentation.getRelationships()
                .stream()
                .map(courseGroupRelationship -> {
                    CourseGroupRelationshipRecord courseGroupRelationshipRecord = new CourseGroupRelationshipRecord(courseGroupRelationship.getCourse().getIndex());
                    courseGroupRelationship.getStudentGroups()
                            .stream()
                            .map(StudentClassInput::getIndex)
                            .collect(Collectors.toCollection(courseGroupRelationshipRecord::getStudentGroup));
                    return courseGroupRelationshipRecord;
                })
                .collect(Collectors.toList());

    }

    private void fillDataWithEmptyTimeSlots(DataInputRepresentation data) {
        TeacherInput emptyTimeSlotsTeacher = new TeacherInput(999999999, EMPTY_TIME_SLOT);

        for (StudentClassInput classInput : data.getStudentClasses()) {
            int occupiedHours = 0;

            for (TeacherInput teacherInput : classInput.getAssignedTeachers().keySet()) {
                occupiedHours += teacherInput.getAssignedStudentClasses().entrySet().
                        stream()
                        .filter(classInputPairEntry -> classInputPairEntry.getKey().equals(classInput))
                        .map(Map.Entry::getValue)
                        .mapToInt(Integer::intValue)
                        .sum();
            }
            if (occupiedHours < data.getTimeslotsPerDay() * 5) {
                emptyTimeSlotsTeacher.addStudentClass(classInput, (data.getTimeslotsPerDay() * 5) - occupiedHours);
            }


        }

        data.addTeacher(emptyTimeSlotsTeacher);
    }

    private static RoomRecord[] mapRooms(DataInputRepresentation dataInputRepresentation) {
        RoomRecord[] teachersReturn = new RoomRecord[dataInputRepresentation.getRooms().size()];

        List<RoomInput> rooms = dataInputRepresentation
                .getRooms()
                .stream()
                .sorted(Comparator.comparing(RoomInput::getRoomIndex))
                .collect(Collectors.toList());

        for (int i = 0; i < rooms.size(); i++) {
            RoomInput roomInput = rooms.get(i);
            teachersReturn[i] = new RoomRecord(roomInput.getRoomIndex(), roomInput.getName(), roomInput.getCapacity());
        }

        return teachersReturn;
    }

    private static Teacher[] mapTeachers(DataInputRepresentation dataInputRepresentation) {
        Teacher[] teachersReturn = new Teacher[dataInputRepresentation.getTeachers().size()];

        int hoursPerDay = dataInputRepresentation.getTimeslotsPerDay();

        List<TeacherInput> teachers = dataInputRepresentation
                .getTeachers()
                .stream()
                .sorted(Comparator.comparing(TeacherInput::getIndex))
                .collect(Collectors.toList());

        for (int i = 0; i < teachers.size(); i++) {
            TeacherInput teacherInput = teachers.get(i);
            Teacher teacher = new Teacher(dataInputRepresentation.getTimeslotsPerDay() * 5);

            teacher.surname = teacherInput.getName();
            teacher.availableDays = 5;
            teacher.availabilityHours = 5 * dataInputRepresentation.getTimeslotsPerDay();
            teacher.totalHours = 0;
            teacherInput.getAssignedStudentClasses()
                    .entrySet()
                    .stream()
                    .sorted(Comparator.comparing(o -> o.getKey().getIndex()))
                    .forEach(classInputPairEntry -> {
                        teacher.classesHeTeaches[teacher.numberOfClasses][0] = classInputPairEntry.getKey().getIndex();
                        teacher.classesHeTeaches[teacher.numberOfClasses][1] = classInputPairEntry.getValue();
                        teacher.classesHeTeaches[teacher.numberOfClasses][2] = 1;
                        teacher.totalHours += classInputPairEntry.getValue();
                        teacher.numberOfClasses++;
                    });

            teacherInput.getUnavailabilityIntervals()
                    .entrySet()
                    .stream()
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .flatMap(integerListEntry -> integerListEntry.getValue()
                            .stream()
                            .map(intervalOfDay -> integerListEntry.getKey() * dataInputRepresentation.getTimeslotsPerDay() + intervalOfDay))
                    .forEach(integer -> teacher.unavailableTimeslots[integer] = 1);


            for (int start = 0; start < (5 * hoursPerDay) - 1; start = start + hoursPerDay) {
                int subtract_day = 0;
                for (int t = start; t < start + hoursPerDay; t++) {
                    if (teacher.unavailableTimeslots[t] == 1) {
                        subtract_day++;
                    }

                    if (subtract_day == hoursPerDay) {
                        teacher.availableDays--;
                        teacher.isAvailableAtDay[start / hoursPerDay] = -1;
                    }
                }
            }

            teachersReturn[i] = teacher;
        }


        return teachersReturn;
    }

    private static StudentGroup[] mapClass(DataInputRepresentation dataInputRepresentation) {
        List<StudentClassInput> classes = dataInputRepresentation.getStudentClasses()
                .stream()
                .sorted(Comparator.comparing(StudentClassInput::getIndex))
                .collect(Collectors.toList());

        StudentGroup[] classRecords = new StudentGroup[dataInputRepresentation.getStudentClasses().size()];

        for (int i = 0; i < classes.size(); i++) {
            StudentClassInput classInput = classes.get(i);
            StudentGroup classRecord = new StudentGroup();
            classRecord.classSequence = i;
            classRecord.className = classInput.getName();
            classRecord.numberOfStudents = classInput.getNumberOfStudents();
            classInput.getAssignedTeachers()
                    .entrySet()
                    .stream()
                    .sorted(Comparator.comparing(o -> o.getKey().getIndex()))
                    .forEach(keyValue -> {
                        classRecord.teachersOfClassAndHours[classRecord.numberOfTeachers][0] = keyValue.getKey().getIndex();
                        classRecord.teachersOfClassAndHours[classRecord.numberOfTeachers][1] = keyValue.getValue();
                        classRecord.teachersOfClassAndHours[classRecord.numberOfTeachers][2] = 1;
                        classRecord.numberOfTeachers++;
                    });


            classRecords[i] = classRecord;
        }


        return classRecords;
    }

    public CalendarOutput mapToOutput(DataInputRepresentation data, CalendarData calendarData, int[][][] result) {
        CalendarOutput calendarOutput = new CalendarOutput(calendarData.getNumberOfSlotsPerDay());
        data.getStudentClasses()
                .stream()
                .filter(studentClass -> !calendarData.isSemianGroup(studentClass.getIndex()))
                .map(classInput -> new StudentClassOutput(classInput.getIdentifier(), classInput.getName(), classInput.getNumberOfStudents()))
                .collect(Collectors.toCollection(calendarOutput::getStudentClassOutputs));

        data.getTeachers()
                .stream()
                .map(teacherInput -> new TeacherOutput(teacherInput.getIdentifier(), teacherInput.getName()))
                .collect(Collectors.toCollection(calendarOutput::getTeachers));

        data.getRooms()
                .stream()
                .map(roomInput -> new RoomOutput(roomInput.getIdentifier(), roomInput.getName(), roomInput.getCapacity()))
                .collect(Collectors.toCollection(calendarOutput::getRooms));


        data.getStudentClasses()
                .stream()
                .sorted(Comparator.comparing(StudentClassInput::getNumberOfStudents))
                .forEach(classInput -> {
                    if (calendarData.isSemianGroup(classInput.getIndex())) {
                        CourseGroupRelationship relationshipByIndex = data.getRelationshipByIndex(classInput.getIndex());
                        for (StudentClassInput studentClass : relationshipByIndex.getStudentGroups()) {
                            StudentClassOutput classOutput = calendarOutput.getStudentClassByIdentifier(studentClass.getIdentifier());
                            mapDataToStudentClass(data, calendarData, result[classInput.getIndex()], calendarOutput, classOutput);
                        }
                    } else {
                        StudentClassOutput classOutput = calendarOutput.getStudentClassByIdentifier(classInput.getIdentifier());
                        mapDataToStudentClass(data, calendarData, result[classInput.getIndex()], calendarOutput, classOutput);
                    }
                });
        return calendarOutput;
    }

    private void mapDataToStudentClass(DataInputRepresentation data, CalendarData calendarData, int[][] ints, CalendarOutput calendarOutput, StudentClassOutput classOutput) {
        for (int j = 0; j < data.getTimeslotsPerDay() * 5; j++) {
            int teacherPosition = ints[j][0];
            int roomPosition = ints[j][1];

            TeacherOutput teacherOutput = calendarOutput.getTeacherByIdentifier(data.getTeacherByIndex(teacherPosition).getIdentifier());
            RoomOutput roomOutput = calendarOutput.getRoomByIdentifier(data.getRoomByIndex(roomPosition).getIdentifier());
            classOutput.addTeachingSession(teacherOutput, j, roomOutput);
        }
    }
}
