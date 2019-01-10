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
import java.util.ArrayList;
import java.util.List;
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

    public TimetableResult triggerCalendarGenerator(Long id) {
        TimetableConfig timetableConfig = timetableConfigRepository.findById(id).orElseThrow(() -> new RuntimeException("Config not found!"));
        TimetableResult timetableResult = timetableGenerator.generateTimeTable(timetableConfig.getConfig());
        TimetableResult save = timetableResultRepository.save(timetableResult);
        slotReservationRepository.saveAll(timetableResult.getSlotReservations());
        return save;
    }

    public TimetableResult getTimetableResult(Long id) {
        return timetableResultRepository.findById(id).orElseThrow(() -> new RuntimeException("Result now fount!"));
    }

    public List<SlotReservationDTO> getTimetableForClass(Long id, Long classId) {
        TimetableResult timetableResult = timetableResultRepository.findById(id).orElseThrow(() -> new RuntimeException("Result now fount!"));

        return timetableResult.getSlotReservations()
                .stream()
                .filter(slotReservation -> slotReservation.getStudentClass().getId().equals(classId))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
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
