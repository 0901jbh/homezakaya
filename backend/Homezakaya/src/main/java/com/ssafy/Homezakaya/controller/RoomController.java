package com.ssafy.Homezakaya.controller;

import com.ssafy.Homezakaya.model.dto.RoomDto;
import com.ssafy.Homezakaya.model.service.RoomServiceImpl;
import com.ssafy.Homezakaya.model.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/rooms")
@RestController
public class RoomController {
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";
    @Autowired
    private RoomServiceImpl roomService;

    @Autowired
    private UserServiceImpl userService;


    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody RoomDto room) {
        Map<String, Object> resultMap = new HashMap<>();
        try{
            // 존재하지 않는 유저 아이디
            if(userService.getUser(room.getHostId()) == null){
                resultMap.put("message", "존재하지 않는 userId");
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
            }

            room.setCreatedTime(LocalDateTime.now());
            boolean res = roomService.createRoom(room);
            return new ResponseEntity<>(room, HttpStatus.CREATED);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<?> getRooms() {
        Map<String, Object> resultMap = new HashMap<>();
        List<RoomDto> rooms = roomService.getRooms();
        resultMap.put("roomList", rooms);
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable int roomId) {
        RoomDto room = roomService.getRoom(roomId);
        try{
            if(room != null) {
                return new ResponseEntity<>(room, HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/password")
    public ResponseEntity<?> checkPassword(@RequestBody RoomDto room){
        Map<String, Object> resultMap = new HashMap<>();
        try{
            if(roomService.getRoom(room.getRoomId()) == null){
                resultMap.put("message", "존재하지 않는 roomId");
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
            }
            String pwd = roomService.getPassword(room.getRoomId());
            if(room.getPassword().equals(pwd)){
                resultMap.put("message", SUCCESS);
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
            }
            else{
                resultMap.put("message", "비밀번호 불일치");
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

    }

    @PutMapping("/enter/{roomId}")
    public ResponseEntity<?> enterRoom(@PathVariable int roomId) {
        Map<String, Object> resultMap = new HashMap<>();
        RoomDto room = roomService.getRoom(roomId);
        if(room == null){
            resultMap.put("message", "존재하지 않는 roomId");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
        }

        if(room.getPersonLimit() > room.getPersonCount()){
            boolean res = roomService.enterRoom(roomId);
            if (res) {
                resultMap.put("message", SUCCESS);
                resultMap.put("personCount", room.getPersonCount() + 1);
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
            } else {
                return ResponseEntity.internalServerError().build();
            }
        }
        else{
            resultMap.put("message", "꽉 찬 방");
            resultMap.put("personCount", room.getPersonCount());
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/quit/{roomId}")
    public ResponseEntity<?> quitRoom(@PathVariable int roomId){
        Map<String, Object> resultMap = new HashMap<>();
        RoomDto room = roomService.getRoom(roomId);
        if(room == null){
            resultMap.put("message", "존재하지 않는 roomId");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
        }

        boolean res = roomService.quitRoom(roomId);
        if(res){
            resultMap.put("message", SUCCESS);
            resultMap.put("personCount", room.getPersonCount() - 1);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
        }
        else
            return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{roomId}")
    private ResponseEntity<?> removeRoom(@PathVariable int roomId){
        Map<String, Object> resultMap = new HashMap<>();
        if(roomService.getRoom(roomId) == null){
            resultMap.put("message", "존재하지 않는 roomId");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
        }
        boolean res = roomService.removeRoom(roomId);

        if(res)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.internalServerError().build();
    }
}