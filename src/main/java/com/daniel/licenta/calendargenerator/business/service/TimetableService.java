package com.daniel.licenta.calendargenerator.business.service;

import com.daniel.licenta.calendargenerator.api.model.TimetableDTO;
import com.daniel.licenta.calendargenerator.business.calendargenerator.TimetableGenerator;
import com.daniel.licenta.calendargenerator.business.model.Course;
import com.daniel.licenta.calendargenerator.business.model.Room;
import com.daniel.licenta.calendargenerator.business.model.RoomReservation;
import com.daniel.licenta.calendargenerator.integration.repo.CourseRepository;
import com.daniel.licenta.calendargenerator.integration.repo.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimetableService {

    @Autowired
    private TimetableGenerator timetableGenerator;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private RoomRepository roomRepository;

    public List<RoomReservation> generateTimetable(TimetableDTO timeTableDTO) {
        List<Course> courses = courseRepository.findAllById(timeTableDTO.getCourseIds());
        List<Room> rooms = roomRepository.findAllById(timeTableDTO.getRoomIds());

        return timetableGenerator.generateTimetable(courses, rooms, timeTableDTO.getWeekSpan());
    }
}
