package com.daniel.licenta.calendargenerator.business.calendargenerator;

import com.daniel.licenta.calendargenerator.business.model.Course;
import com.daniel.licenta.calendargenerator.business.model.Room;
import com.daniel.licenta.calendargenerator.business.model.RoomReservation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TimetableGenerator {
    public List<RoomReservation> generateTimetable(List<Course> courses, List<Room> rooms, Long weekSpan) {
        return new ArrayList<>();
    }
}
