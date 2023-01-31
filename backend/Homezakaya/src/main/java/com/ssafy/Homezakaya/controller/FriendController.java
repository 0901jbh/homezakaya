package com.ssafy.Homezakaya.controller;

import com.ssafy.Homezakaya.model.dto.FriendDto;
import com.ssafy.Homezakaya.model.dto.UserDto;
import com.ssafy.Homezakaya.model.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    @Autowired
    private FriendService friendService;

    // 친구 목록 조회(친구리스트 반환)
    @GetMapping("/{userId}")
    public ResponseEntity<List<UserDto>> getFriendsList(@PathVariable("userId") String userId) {
        List<UserDto> list = friendService.getFriendsById(userId);
        try {
            if(list == null){
                return new ResponseEntity<List<UserDto>>(list, HttpStatus.NOT_FOUND);
            }else {
                return new ResponseEntity<List<UserDto>>(list, HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<UserDto>>(list, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 친구 끊기(삭제)
    @DeleteMapping("/{userAId}/{userBId}")
    public ResponseEntity<?> friendDelete(@PathVariable String userAId, @PathVariable String userBId) {

        int result = 0;
        Map<String, Object> resultMap = new HashMap<>();
        FriendDto fDto = new FriendDto(userAId, userBId, true);
        try {
            result = friendService.removeFriend(fDto);
            if (result == 1) {
                resultMap.put("message", SUCCESS);
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
            }
            else {
                resultMap.put("message", "유저 아이디 또는 친구 관계가 존재하지 않음");
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 친구 요청 처리
    @PostMapping("/request")
    public ResponseEntity<?> friendRequest(@RequestBody FriendDto friendDto) {
        int result = 0;
        Map<String, Object> resultMap = new HashMap<>();

        try {
            result = friendService.createFriend(friendDto);
            if (result == 1) {
                resultMap.put("message", SUCCESS);
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.CREATED);
            }else {
                resultMap.put("message", "존재하지 않는 유저");
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 친구 요청 리스트 조회
    @GetMapping("/request/{userId}")
    public ResponseEntity<List<UserDto>> getFriendsReqList(@PathVariable("userId") String userId) throws SQLException {
        List<UserDto> list = friendService.getFriendReqById(userId);
        try {
            if(list == null){
                return new ResponseEntity<List<UserDto>>(list, HttpStatus.NOT_FOUND);
            }else {
                return new ResponseEntity<List<UserDto>>(list, HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<UserDto>>(list, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // 친구 요청 수락
    @PutMapping("/request")
    public ResponseEntity<?> friendReqAccept(@RequestBody FriendDto friendDto) {
        Map<String, Object> resultMap = new HashMap<>();
        int result = 0;
        try {
            result = friendService.modifyFriend(friendDto);
            if (result == 1) {
                resultMap.put("message", SUCCESS);
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.CREATED);
            }else{
                resultMap.put("message", "존재하지 않는 유저");
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 친구 요청 거절/취소
    @DeleteMapping("/request/{userAId}/{userBId}")
    public ResponseEntity<?> friendReqReject(@PathVariable String userAId, @PathVariable String userBId) {
        int result = 0;
        Map<String, Object> resultMap = new HashMap<>();
        FriendDto fDto = new FriendDto(userAId, userBId, false);
        try {
            result = friendService.removeFriendReq(fDto);
            if (result == 1) {
                resultMap.put("message", SUCCESS);
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
            } else {
                resultMap.put("message", "유저아이디 또는 요청이 존재하지 않음");
                return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // 유저 조회(검색)
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable("keyword") String keyword) throws SQLException {
        List<UserDto> list = friendService.getUserByKeyword(keyword);
        return new ResponseEntity<List<UserDto>>(list, HttpStatus.OK);
    }

}