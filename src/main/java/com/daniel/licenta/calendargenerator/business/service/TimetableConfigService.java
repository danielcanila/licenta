package com.daniel.licenta.calendargenerator.business.service;

import com.daniel.licenta.calendargenerator.algorithm.inputmodel.ConfigData;
import com.daniel.licenta.calendargenerator.business.common.GenericMapper;
import com.daniel.licenta.calendargenerator.business.model.Room;
import com.daniel.licenta.calendargenerator.business.model.StudentClass;
import com.daniel.licenta.calendargenerator.business.model.TimetableConfig;
import com.daniel.licenta.calendargenerator.business.model.json.SemiyearRelationship;
import com.daniel.licenta.calendargenerator.business.model.json.StudentData;
import com.daniel.licenta.calendargenerator.business.model.json.TimeableConfigData;
import com.daniel.licenta.calendargenerator.integration.repo.RoomRepository;
import com.daniel.licenta.calendargenerator.integration.repo.StudentClassRepository;
import com.daniel.licenta.calendargenerator.integration.repo.TimetableConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TimetableConfigService {

    @Autowired
    private TimetableConfigRepository timetableConfigRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private StudentClassRepository studentClassRepository;


    @Autowired
    private GenericMapper<ConfigData, ConfigData> genericMapper;

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

    public TimetableConfig addRooms(long id, List<Long> roomIds) {
        TimetableConfig timetableConfig = timetableConfigRepository.findById(id).orElseThrow(() -> new RuntimeException("Config not found!"));

        timetableConfig.getConfig().getRoomIds().addAll(roomIds);
        return timetableConfigRepository.save(timetableConfig);
    }

    public TimetableConfig addStudentclasses(long id, List<Long> studentClassIds) {
        TimetableConfig timetableConfig = timetableConfigRepository.findById(id).orElseThrow(() -> new RuntimeException("Config not found!"));

        List<StudentClass> studentClasses = studentClassRepository.findAllById(studentClassIds);

        List<StudentData> classes = studentClasses
                .stream()
                .map(studentClass -> new StudentData())
                .collect(Collectors.toList());

        studentClasses.stream()
                .filter(studentClass -> studentClass.hasChildren())
                .map(studentClass -> {
                    SemiyearRelationship semiyearRelationship = new SemiyearRelationship(studentClass.getId());
                    studentClass.getStudentClasses()
                            .stream()
                            .map(studentClass1 -> studentClass.getId())
                            .collect(Collectors.toCollection(() -> semiyearRelationship.getClassIds()));
                    return semiyearRelationship;
                })
                .collect(Collectors.toCollection(() -> timetableConfig.getConfig().getRelationships()));

        return timetableConfig;
    }
}
