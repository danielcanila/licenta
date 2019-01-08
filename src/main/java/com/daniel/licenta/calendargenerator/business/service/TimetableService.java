package com.daniel.licenta.calendargenerator.business.service;

import com.daniel.licenta.calendargenerator.algorithm.inputmodel.ConfigData;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.CalendarOutput;
import com.daniel.licenta.calendargenerator.business.calendargenerator.TimetableGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimetableService {

    @Autowired
    private TimetableGenerator timetableGenerator;

    public CalendarOutput generateTimetable(ConfigData configData) {
        return timetableGenerator.generateTimeTable(configData);
    }
}
