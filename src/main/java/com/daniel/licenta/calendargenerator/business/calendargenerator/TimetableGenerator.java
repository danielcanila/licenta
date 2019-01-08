package com.daniel.licenta.calendargenerator.business.calendargenerator;

import com.daniel.licenta.calendargenerator.algorithm.TimeTableGeneratorCSO;
import com.daniel.licenta.calendargenerator.algorithm.extra.Data;
import com.daniel.licenta.calendargenerator.algorithm.inputmodel.ConfigData;
import com.daniel.licenta.calendargenerator.algorithm.inputmodel.DataInputRepresentation;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.CalendarOutput;
import com.daniel.licenta.calendargenerator.business.model.RoomReservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TimetableGenerator {

    @Autowired
    private TimeTableGeneratorCSO timeTableGeneratorCSO;

    public CalendarOutput generateTimeTable(ConfigData configData) {
        DataInputRepresentation dataInputRepresentation = Data.facultyExampleTwo();
        return timeTableGeneratorCSO.generateTimetable(dataInputRepresentation,configData);
    }
}
