package com.daniel.licenta.calendargenerator.algorithm.outputmodel;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class CalendarOutput {

    private int slotsPerDay;
    private List<StudentClassOutput> studentClassOutputs = new ArrayList<>();
    private List<TeacherOutput> teachers = new ArrayList<>();
    private List<RoomOutput> rooms = new ArrayList<>();

    public CalendarOutput(int slotsPerDay) {
        this.slotsPerDay = slotsPerDay;
    }

    public TeacherOutput getTeacherByIdentifier(int identifier) {
        return teachers.stream().filter(t -> t.getIdentifier() == identifier).findFirst().get();
    }

    public StudentClassOutput getStudentClassByIdentifier(int identifier) {
        return studentClassOutputs.stream().filter(c -> c.getIdentifier() == identifier).findFirst().get();
    }
    public RoomOutput getRoomByIdentifier(int identifier) {
        return rooms.stream().filter(c -> c.getIdentifier() == identifier).findFirst().get();
    }
}
