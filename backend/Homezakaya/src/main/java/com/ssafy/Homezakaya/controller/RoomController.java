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
    public ResponseEntity<?> registRoom(@RequestBody RoomDto room) {
        room.setCreatedTime(LocalDateTime.now());
        boolean res = roomService.insertRoom(room);

        if(res)
            return ResponseEntity.ok(room);
        else
            return ResponseEntity.internalServerError().build();
    }

    @GetMapping()
    public ResponseEntity<?> getRoomList() {
        List<RoomDto> rooms = roomService.getRooms();
        return ResponseEntity.ok(rooms);
    }


    //비번, 입장, 퇴장
    @GetMapping()
    public ResponseEntity<?> checkPassword(@RequestBody RoomDto room){
        boolean res = roomService.checkPassword(room);

        if(res)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.noContent().build();
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
        boolean res = roomService.deleteRoom(roomId);

        if(res)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.internalServerError().build();
    }
}