package com.ssafy.Homezakaya.controller;

import com.ssafy.Homezakaya.model.dto.InviteFriendDto;
import com.ssafy.Homezakaya.model.dto.UserInRoomDto;
import com.ssafy.Homezakaya.model.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private InviteFriendServiceImpl inviteFriendService;

    @PostMapping
    public ResponseEntity<?> createUserInRoom(@RequestBody UserInRoomDto userInRoom) {
        Map<String, Object> resultMap = new HashMap<>();
        // 아이디가 존재하지 않음
        if (userService.getUser(userInRoom.getUserId()) == null) {
            resultMap.put("message", "존재하지 않는 userId");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
        }

        // 해당 roomId가 존재하지 않음
        if (roomService.getRoom(userInRoom.getRoomId()) == null) {
            resultMap.put("message", "존재하지 않는 roomId");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
        }

        // 해당 유저가 이미 방에 참가한 상태
        if (userInRoomService.getRoomId(userInRoom.getUserId()) != null) {
            resultMap.put("message", "이미 해당 room에 참가한 유저입니다.");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.CONFLICT);
        }

        boolean res = userInRoomService.createUserInRoom(userInRoom);

        if (res)
            return ResponseEntity.created(URI.create("")).body(userInRoom);
        else
            return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> removeUserInRoom(@PathVariable String userId) {
        Map<String, Object> resultMap = new HashMap<>();
        // 아이디가 존재하지 않음
        if (userService.getUser(userId) == null) {
            resultMap.put("message", "존재하지 않는 userId");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
        }

        // 유저가 room에 참가한 상태가 아님
        if (userInRoomService.getRoomId(userId) == null) {
            resultMap.put("message", "room에 참가한 유저가 아닙니다.");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
        }

        boolean res = userInRoomService.removeUserInRoom(userId);

        if (res)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getRoomId(@PathVariable String userId) {
        Map<String, Object> resultMap = new HashMap<>();
        // 아이디가 존재하지 않음
        if (userService.getUser(userId) == null) {
            resultMap.put("message", "존재하지 않는 userId");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
        }

        // 유저가 room에 참가한 상태가 아님
        if (userInRoomService.getRoomId(userId) == null) {
            resultMap.put("message", "room에 참가한 유저가 아닙니다.");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
        }

        Integer res = userInRoomService.getRoomId(userId);

        if (res != null)
            return ResponseEntity.ok(res);
        else
            return ResponseEntity.internalServerError().build();
    }

    // 방에서 친구초대
    @PostMapping("/invite")
    public ResponseEntity<?> createInvite(@RequestBody InviteFriendDto inviteFriendDto) {
        Map<String, Object> resultMap = new HashMap<>();
        boolean res = inviteFriendService.createInvite(inviteFriendDto);
        try {
            if (res) {
                resultMap.put("message", "친구를 방으로 초대했습니다.");
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
            } else {
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            resultMap.put("message", "이미 초대를 보냈습니다.");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // 초대한 유저 id만 조회 (fromUser) - xx님이 초대 요청을 보냈습니다.
    @GetMapping("/invite/{toUserId}")
    public ResponseEntity<?> getInvitesList(@PathVariable String toUserId) {
        Map<String, Object> resultMap = new HashMap<>();
        List<String> invitesFromUser = inviteFriendService.getInvites(toUserId);

        System.out.println(invitesFromUser.toString());
        System.out.println(invitesFromUser);

        try {
            if (invitesFromUser.size() == 0) {
                resultMap.put("message", "받은 초대가 없습니다.");
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NO_CONTENT);   // 204
            } else {
                return new ResponseEntity<List<String>>(invitesFromUser, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<String>>(invitesFromUser, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/invite/{fromUserId}/{toUserId}")
    public ResponseEntity<?> removeInvite(@PathVariable String fromUserId, @PathVariable String toUserId) {
        Map<String, Object> resultMap = new HashMap<>();
        List<String> invitesFromUser = inviteFriendService.getInvites(toUserId);    // 초대한 유저 리스트
        try {
            if (!invitesFromUser.isEmpty()) {
                InviteFriendDto invite = new InviteFriendDto(fromUserId, toUserId);
                inviteFriendService.removeInvite(invite);
                resultMap.put("message", "방 초대 요청 삭제 완료");
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
            } else {
                resultMap.put("message", "방 초대가 없습니다.");
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}