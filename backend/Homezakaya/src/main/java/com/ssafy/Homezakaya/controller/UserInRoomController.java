package com.ssafy.Homezakaya.controller;

import com.ssafy.Homezakaya.model.dto.UserInRoomDto;
import com.ssafy.Homezakaya.model.service.RoomServiceImpl;
import com.ssafy.Homezakaya.model.service.UserInRoomServiceImpl;
import com.ssafy.Homezakaya.model.service.UserService;
import com.ssafy.Homezakaya.model.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/userinroom")
@RestController
public class UserInRoomController {

    @Autowired
    private UserInRoomServiceImpl userInRoomService;

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private RoomServiceImpl roomService;

    @PostMapping
    public ResponseEntity<?> createUserInRoom(@RequestBody UserInRoomDto userInRoom) {
        Map<String, Object> resultMap = new HashMap<>();
        // 아이디가 존재하지 않음
        if(userService.getUser(userInRoom.getUserId()) == null){
            resultMap.put("message", "존재하지 않는 userId");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
        }

        // 해당 roomId가 존재하지 않음
        if(roomService.getRoom(userInRoom.getRoomId()) == null){
            resultMap.put("message", "존재하지 않는 roomId");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
        }

        // 해당 유저가 이미 방에 참가한 상태
        if(userInRoomService.getRoomId(userInRoom.getUserId()) != null){
            resultMap.put("message", "이미 해당 room에 참가한 유저입니다.");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.CONFLICT);
        }

        boolean res = userInRoomService.createUserInRoom(userInRoom);

        if(res)
            return ResponseEntity.created(URI.create("")).body(userInRoom);
        else
            return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> removeUserInRoom(@PathVariable String userId) {
        Map<String, Object> resultMap = new HashMap<>();
        // 아이디가 존재하지 않음
        if(userService.getUser(userId) == null){
            resultMap.put("message", "존재하지 않는 userId");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
        }

        // 유저가 room에 참가한 상태가 아님
        if(userInRoomService.getRoomId(userId) == null){
            resultMap.put("message", "room에 참가한 유저가 아닙니다.");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
        }

        boolean res = userInRoomService.removeUserInRoom(userId);

        if(res)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getRoomId(@PathVariable String userId){
        Map<String, Object> resultMap = new HashMap<>();
        // 아이디가 존재하지 않음
        if(userService.getUser(userId) == null){
            resultMap.put("message", "존재하지 않는 userId");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
        }

        // 유저가 room에 참가한 상태가 아님
        if(userInRoomService.getRoomId(userId) == null){
            resultMap.put("message", "room에 참가한 유저가 아닙니다.");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
        }

        Integer res = userInRoomService.getRoomId(userId);

        if(res != null)
            return ResponseEntity.ok(res);
        else
            return ResponseEntity.internalServerError().build();
    }
}