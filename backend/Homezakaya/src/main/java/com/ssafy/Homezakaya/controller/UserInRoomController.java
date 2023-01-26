package com.ssafy.Homezakaya.controller;

import com.ssafy.Homezakaya.model.dto.UserInRoomDto;
import com.ssafy.Homezakaya.model.service.UserInRoomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/userinroom")
@RestController
public class UserInRoomController {

    @Autowired
    private UserInRoomServiceImpl userInRoomService;

    @PostMapping
    public ResponseEntity<?> createUserInRoom(@RequestBody UserInRoomDto userInRoom) {
        boolean res = userInRoomService.createUserInRoom(userInRoom);

        if(res)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> removeUserInRoom(@PathVariable String userId) {
        boolean res = userInRoomService.removeUserInRoom(userId);

        if(res)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getRoomId(@PathVariable String userId){
        int res = userInRoomService.getRoomId(userId);
        if(res > 0)
            return ResponseEntity.ok(res);
        else
            return ResponseEntity.internalServerError().build();
    }
}