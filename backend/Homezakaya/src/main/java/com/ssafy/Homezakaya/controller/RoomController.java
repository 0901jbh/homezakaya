package com.ssafy.Homezakaya.controller;

import com.ssafy.Homezakaya.model.dto.RoomDto;
import com.ssafy.Homezakaya.model.service.RoomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api/rooms")
@RestController
public class RoomController {

    @Autowired
    private RoomServiceImpl roomService;

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody RoomDto room) {
        int roomId = roomService.createRoom(room);
        try{
            if(roomId > 0) {
                RoomDto newRoom = roomService.getRoom(roomId);
                return ResponseEntity.ok(newRoom);
            }
            else
                return ResponseEntity.notFound().build();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping()
    public ResponseEntity<?> getRooms() {
        List<RoomDto> rooms = roomService.getRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable int roomId) {
        RoomDto room = roomService.getRoom(roomId);
        return ResponseEntity.ok(room);
    }

    @PostMapping("/password")
    public ResponseEntity<?> checkPassword(@RequestBody RoomDto room){
        String pwd = roomService.getPassword(room.getRoomId());

        if(pwd.equals(room.getPassword())) {
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.internalServerError().build();
        }

    }

    @PutMapping("/enter/{roomId}")
    public ResponseEntity<?> enterRoom(@PathVariable int roomId){
        boolean res = roomService.enterRoom(roomId);

        if(res)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.internalServerError().build();
    }

    @PutMapping("/quit/{roomId}")
    public ResponseEntity<?> quitRoom(@PathVariable int roomId){
        boolean res = roomService.quitRoom(roomId);

        if(res)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{roomId}")
    private ResponseEntity<?> removeRoom(@PathVariable int roomId){
        boolean res = roomService.removeRoom(roomId);

        if(res)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.internalServerError().build();
    }
}