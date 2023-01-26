package com.ssafy.Homezakaya.controller;

import com.ssafy.Homezakaya.model.dto.UserDto;
import com.ssafy.Homezakaya.model.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RequestMapping("/api/users")
@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    // 확인용
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    // 회원 정보 등록
    @PostMapping
    public ResponseEntity<String> createUser(UserDto userDto) {
        userService.createUser(userDto);
        System.out.println("userDto = " + userDto);

        return new ResponseEntity<>(SUCCESS, HttpStatus.CREATED);
    }

    // 회원 정보 조회
    @GetMapping("/id/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId) {
        if (userService.getUser(userId) == null) {
            return new ResponseEntity<String>(FAIL, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<UserDto>(userService.getUser(userId), HttpStatus.OK);
    }

    // 회원 정보 수정
    @PutMapping("/{userId}")
    public ResponseEntity<?> modifyUser(UserDto userDto) {
        userService.modifyUser(userDto);
        return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
    }

    // 회원 정보 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> removeUser(@PathVariable String userId) {
        if (userService.removeUser(userId)) {
            return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
        }
        return new ResponseEntity<>(FAIL, HttpStatus.NO_CONTENT);
    }

    // 매너 도수 누적
    @PutMapping("/point/{userId}")
    public ResponseEntity<String> addMannerPoint(@PathVariable String userId, double mannerPointSum) {
        userService.addMannerPoint(userId, mannerPointSum);
        return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
    }

    // 매너 도수 조회
    @GetMapping("/point/{userId}")
    public ResponseEntity<Double> averageOfMannerPoint(@PathVariable String userId) {
        return new ResponseEntity<Double>(userService.averageOfMannerPoint(userId), HttpStatus.OK);
    }

    // 로그인

    // 로그아웃

}
