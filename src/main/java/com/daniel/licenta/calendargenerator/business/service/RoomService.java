package com.daniel.licenta.calendargenerator.business.service;

import com.daniel.licenta.calendargenerator.business.common.GenericMapper;
import com.daniel.licenta.calendargenerator.business.model.Room;
import com.daniel.licenta.calendargenerator.integration.repo.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private GenericMapper<Room, Room> genericMapper;

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }

    public void delete(Long id) {
        roomRepository.deleteById(id);
    }

    public Room update(Long id, Room room) {
        Room toSave = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found!"));

        genericMapper.map(room, toSave);

        return roomRepository.save(toSave);
    }
}
