package com.daniel.licenta.calendargenerator.api.controller;

import com.daniel.licenta.calendargenerator.business.model.Room;
import com.daniel.licenta.calendargenerator.business.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    public List<Room> getRooms() {
        return roomService.findAll();
    }

    @PostMapping
    public Room addRoom(@RequestBody Room room) {
        return roomService.save(room);
    }

    @DeleteMapping("{id}")
    public String deleteRoom(@PathVariable("id") Long id) {
        roomService.delete(id);
        return "Item deleted.";
    }

    @PatchMapping("{id}")
    public Room updateRoom(@PathVariable("id") Long id, @RequestBody Room room) {
        return roomService.update(id, room);
    }
}
