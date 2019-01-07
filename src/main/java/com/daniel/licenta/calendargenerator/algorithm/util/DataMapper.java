package com.daniel.licenta.calendargenerator.algorithm.util;

import com.daniel.licenta.calendargenerator.algorithm.inputmodel.StudentClass;
import com.daniel.licenta.calendargenerator.algorithm.inputmodel.DataInputRepresentation;
import com.daniel.licenta.calendargenerator.algorithm.inputmodel.RoomInput;
import com.daniel.licenta.calendargenerator.algorithm.inputmodel.TeacherInput;
import com.daniel.licenta.calendargenerator.algorithm.model.CalendarData;
import com.daniel.licenta.calendargenerator.algorithm.model.StudentGroup;
import com.daniel.licenta.calendargenerator.algorithm.model.RoomRecord;
import com.daniel.licenta.calendargenerator.algorithm.model.Teacher;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.daniel.licenta.calendargenerator.algorithm.util.CSOConstants.EMPTY_TIME_SLOT;

@Component
public class DataMapper {

    public void fillDataWithEmptyTimeSlots(DataInputRepresentation data) {
        TeacherInput emptyTimeSlotsTeacher = new TeacherInput(999999999, EMPTY_TIME_SLOT);

        for (StudentClass classInput : data.getStudentClasses()) {
            int occupiedHours = 0;

            for (TeacherInput teacherInput : classInput.getAssignedTeachers().keySet()) {
                occupiedHours += teacherInput.getAssignedStudentClasses().entrySet().
                        stream()
                        .filter(classInputPairEntry -> classInputPairEntry.getKey().equals(classInput))
                        .map(classInputPairEntry -> classInputPairEntry.getValue().getKey())
                        .mapToInt(Integer::intValue)
                        .sum();
            }
            if (occupiedHours < data.getTimeslotsPerDay() * 5) {
                emptyTimeSlotsTeacher.addStudentClass(classInput, (data.getTimeslotsPerDay() * 5) - occupiedHours, 1);
            }


        }

        data.addTeacher(emptyTimeSlotsTeacher);
    }

    public CalendarData mapToCalendarData(DataInputRepresentation dataInputRepresentation) {
        fillDataWithEmptyTimeSlots(dataInputRepresentation);

        StudentGroup[] classRecords = mapClass(dataInputRepresentation);
        Teacher[] teachers = mapTeachers(dataInputRepresentation);
        RoomRecord[] roomRecords = mapRooms(dataInputRepresentation);


        CalendarData calendarData = new CalendarData(classRecords.length, teachers.length);
        calendarData.setTeachers(teachers);
        calendarData.setStudentGroups(classRecords);
        calendarData.setRooms(roomRecords);
        calendarData.setNumberOfSlotsPerDay(dataInputRepresentation.getTimeslotsPerDay());

        return calendarData;
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
                        teacher.classesHeTeaches[teacher.numberOfClasses][1] = classInputPairEntry.getValue().getKey();
                        teacher.classesHeTeaches[teacher.numberOfClasses][2] = classInputPairEntry.getValue().getValue();
                        teacher.totalHours += classInputPairEntry.getValue().getKey();
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
        List<StudentClass> classes = dataInputRepresentation.getStudentClasses()
                .stream()
                .sorted(Comparator.comparing(StudentClass::getIndex))
                .collect(Collectors.toList());

        StudentGroup[] classRecords = new StudentGroup[dataInputRepresentation.getStudentClasses().size()];

        for (int i = 0; i < classes.size(); i++) {
            StudentClass classInput = classes.get(i);
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
                        classRecord.teachersOfClassAndHours[classRecord.numberOfTeachers][1] = keyValue.getValue().getKey();
                        classRecord.teachersOfClassAndHours[classRecord.numberOfTeachers][2] = keyValue.getValue().getValue();
                        classRecord.numberOfTeachers++;
                    });


            classRecords[i] = classRecord;
        }


        return classRecords;
    }
}
