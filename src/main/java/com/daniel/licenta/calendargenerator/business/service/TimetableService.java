package com.daniel.licenta.calendargenerator.business.service;

import com.daniel.licenta.calendargenerator.api.model.DataDTO;
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

import java.time.LocalDateTime;
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
//        new Thread(() -> {
            TimetableConfig timetableConfig = timetableConfigRepository.findById(id).orElseThrow(() -> new RuntimeException("Config not found!"));
            TimetableResult timetableResult = timetableGenerator.generateTimeTable(timetableConfig.getConfig());

            timetableResult.setName(timetableConfig.getName() + " - " + LocalDateTime.now());
            timetableResult.setTimetableConfig(timetableConfig);
            timetableResultRepository.save(timetableResult);
            slotReservationRepository.saveAll(timetableResult.getSlotReservations());
//        }).start();
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

    public Map<Long, List<SlotReservationDTO>> getTimetableForTeacher(long id, long teacherId) {
        TimetableResult timetableResult = timetableResultRepository.findById(id).orElseThrow(() -> new RuntimeException("Result now fount!"));

        Map<Long, List<SlotReservationDTO>> collect = timetableResult.getSlotReservations()
                .stream()
                .filter(slotReservation -> slotReservation.getTeacher().getId().equals(teacherId))
                .map(this::mapToDTO)
                .collect(Collectors.groupingBy(SlotReservationDTO::getDay));


        return handleCourseDuplicates(collect);
    }

    public Map<Long, List<SlotReservationDTO>> getTimetableForRoom(long id, long roomId) {
        TimetableResult timetableResult = timetableResultRepository.findById(id).orElseThrow(() -> new RuntimeException("Result now fount!"));

        Map<Long, List<SlotReservationDTO>> collect = timetableResult.getSlotReservations()
                .stream()
                .filter(slotReservation -> slotReservation.getRoom().getId().equals(roomId))
                .map(this::mapToDTO)
                .collect(Collectors.groupingBy(SlotReservationDTO::getDay));

        return handleCourseDuplicates(collect);
    }

    public List<DataDTO> retrieveAllTimetables() {
        return timetableResultRepository.findAll()
                .stream()
                .map(result -> new DataDTO(result.getId(), result.getName()))
                .collect(Collectors.toList());
    }

    public List<DataDTO> retrieveAllTeachersForTimetable(Long id) {
        TimetableResult timetableResult = timetableResultRepository.findById(id).orElseThrow(() -> new RuntimeException("Result now fount!"));

        return timetableResult
                .getSlotReservations()
                .stream()
                .map(slot -> new DataDTO(slot.getTeacher().getId(), slot.getTeacher().getName()))
                .distinct()
                .collect(Collectors.toList());
    }

    public List<DataDTO> retrieveAllStudentClassesForTimetable(Long id) {
        TimetableResult timetableResult = timetableResultRepository.findById(id).orElseThrow(() -> new RuntimeException("Result now fount!"));
        return timetableResult
                .getSlotReservations()
                .stream()
                .map(slot -> new DataDTO(slot.getStudentClass().getId(), slot.getStudentClass().getName()))
                .distinct()
                .collect(Collectors.toList());
    }

    public List<DataDTO> retrieveAllRoomsForTimetable(Long id) {
        TimetableResult timetableResult = timetableResultRepository.findById(id).orElseThrow(() -> new RuntimeException("Result now fount!"));
        return timetableResult
                .getSlotReservations()
                .stream()
                .map(slot -> new DataDTO(slot.getRoom().getId(), slot.getRoom().getName()))
                .distinct()
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

    private Map<Long, List<SlotReservationDTO>> handleCourseDuplicates(Map<Long, List<SlotReservationDTO>> collect) {
        return collect.entrySet()
                .stream()
                .flatMap(dayReservation ->
                        dayReservation.getValue()
                                .stream()
                                .collect(Collectors.groupingBy(SlotReservationDTO::getSlot))
                                .entrySet()
                                .stream()
                                .map(slotReservation -> {
                                    SlotReservationDTO dto = slotReservation.getValue().get(0);
                                    String studentGroups = slotReservation
                                            .getValue()
                                            .stream()
                                            .map(SlotReservationDTO::getStudentClassName)
                                            .collect(Collectors.joining(", "));
                                    dto.setStudentClassName(studentGroups);
                                    return dto;
                                })
                )
                .collect(Collectors.groupingBy(SlotReservationDTO::getDay));
    }
}

