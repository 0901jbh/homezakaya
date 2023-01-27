package com.ssafy.Homezakaya.controller;

import com.ssafy.Homezakaya.model.dto.UserInRoomDto;
import com.ssafy.Homezakaya.model.service.UserInRoomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/api/userinroom")
@RestController
public class UserInRoomController {

    @Autowired
    private UserInRoomServiceImpl userInRoomService;

    @PostMapping
    public ResponseEntity<?> createUserInRoom(@RequestBody UserInRoomDto userInRoom) {
        boolean res = userInRoomService.insertUserInRoom(userInRoom);

        if(res)
            return ResponseEntity.created(URI.create("")).body(userInRoom);
        else
            return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> removeUserInRoom(@PathVariable String userId) {
        boolean res = userInRoomService.deleteUserInRoom(userId);

        if(res)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getRoomId(@PathVariable String userId){
        Integer res = userInRoomService.getRoomId(userId);
        System.out.println(res);
        if(res != null)
            return ResponseEntity.ok(res);
        else
            return ResponseEntity.internalServerError().build();
    }
}