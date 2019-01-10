package com.daniel.licenta.calendargenerator.business.service;

import com.daniel.licenta.calendargenerator.algorithm.inputmodel.ConfigData;
import com.daniel.licenta.calendargenerator.algorithm.outputmodel.CalendarOutput;
import com.daniel.licenta.calendargenerator.api.model.SlotReservationDTO;
import com.daniel.licenta.calendargenerator.business.calendargenerator.TimetableGenerator;
import com.daniel.licenta.calendargenerator.business.model.SlotReservation;
import com.daniel.licenta.calendargenerator.business.model.TimetableConfig;
import com.daniel.licenta.calendargenerator.business.model.TimetableResult;
import com.daniel.licenta.calendargenerator.integration.repo.SlotReservationRepository;
import com.daniel.licenta.calendargenerator.integration.repo.TimetableConfigRepository;
import com.daniel.licenta.calendargenerator.integration.repo.TimetableResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TimetableService {

    @Autowired
    private TimetableConfigRepository timetableConfigRepository;

    @Autowired
    private TimetableGenerator timetableGenerator;

    @Autowired
    private TimetableResultRepository timetableResultRepository;

    @Autowired
    private SlotReservationRepository slotReservationRepository;

    public void triggerCalendarGenerator(Long id) {
        new Thread(() -> {
            TimetableConfig timetableConfig = timetableConfigRepository.findById(id).orElseThrow(() -> new RuntimeException("Config not found!"));
            TimetableResult timetableResult = timetableGenerator.generateTimeTable(timetableConfig.getConfig());

            timetableResult.setName(timetableConfig.getName() + " - " + LocalDateTime.now());
            timetableResult.setTimetableConfig(timetableConfig);
            timetableResultRepository.save(timetableResult);
            slotReservationRepository.saveAll(timetableResult.getSlotReservations());
        }).start();
    }

    public List<TimetableResult> getTimetableResults() {
        List<TimetableResult> found = timetableResultRepository.findAll();

        return found.stream()
                .map(timetableResult -> {
                    TimetableResult result = new TimetableResult();
                    result.setName(timetableResult.getName());
                    result.setId(timetableResult.getId());
                    return result;
                })
                .collect(Collectors.toList());
    }

    public Map<Long, List<SlotReservationDTO>> getTimetableForClass(Long id, Long classId) {
        TimetableResult timetableResult = timetableResultRepository.findById(id).orElseThrow(() -> new RuntimeException("Result now fount!"));

        return timetableResult.getSlotReservations()
                .stream()
                .filter(slotReservation -> slotReservation.getStudentClass().getId().equals(classId))
                .map(this::mapToDTO)
                .collect(Collectors.groupingBy(SlotReservationDTO::getDay));
    }

    private SlotReservationDTO mapToDTO(SlotReservation slotReservation) {
        SlotReservationDTO slotReservationDTO = new SlotReservationDTO();
        slotReservationDTO.setDay(slotReservation.getDay());
        slotReservationDTO.setSlot(slotReservation.getSlot());
        slotReservationDTO.setRoomName(slotReservation.getRoom().getName());
        slotReservationDTO.setRoomCapacity(slotReservation.getRoom().getCapacity());
        slotReservationDTO.setLectureName(slotReservation.getLecture().getName());
        slotReservationDTO.setTeacherName(slotReservation.getTeacher().getName());
        slotReservationDTO.setStudentClassName(slotReservation.getStudentClass().getName());
        return slotReservationDTO;
    }

}
