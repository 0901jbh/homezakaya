package com.ssafy.Homezakaya.controller;

import com.ssafy.Homezakaya.model.dto.UserDto;
import com.ssafy.Homezakaya.model.service.UserServiceImpl;
import com.ssafy.Homezakaya.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Objects;

@RequestMapping("/api/users")
@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private JwtUtil jwtUtil;

    // 확인용
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    // 회원 정보 등록
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserDto user) {
        userService.createUser(user);
        return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
    }

    // id 중복확인
    @GetMapping("/id/{userId}")
    public ResponseEntity<String> checkId(@PathVariable String userId) {
        UserDto user = userService.getUser(userId);
        if (userService.getUser(userId) != null) {
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);  // 400
        }
        return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);  // 200
    }

    // nickname 중복확인
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<String> checkNickName(@PathVariable String nickname) {
        if (userService.checkNickname(nickname) != null) {
            return new ResponseEntity<String>(FAIL, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
    }

    // 회원 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId) {
        if (userService.getUser(userId) == null) {
            return new ResponseEntity<String>(FAIL, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<UserDto>(userService.getUser(userId), HttpStatus.OK);
        }
    }

    // 회원 정보 수정
    @PutMapping("/{userId}")
    public ResponseEntity<?> modifyUser(@RequestBody UserDto user) {
        if (userService.modifyUser(user)) {
            return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(FAIL, HttpStatus.NOT_FOUND);
        }
    }

    // 회원 정보 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> removeUser(@PathVariable String userId) {
        if (userService.removeUser(userId)) {
            return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(FAIL, HttpStatus.NO_CONTENT);
        }
    }

    // 매너 도수 갱신
    @PutMapping("/point/{userId}")
    public ResponseEntity<String> averageOfMannerPoint(@PathVariable String userId, double mannerPointSum) {
        userService.averageOfMannerPoint(userId, mannerPointSum);
        return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
    }

    // 로그인 (jwt 토큰만 생성)
    @PostMapping("/login")
    public ResponseEntity<HashMap<String, Object>> login(@RequestBody UserDto user, HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> result = new HashMap<>();
        HttpStatus status = null;

        UserDto loginUser = userService.getUser(user.getUserId());
        try {
            if (loginUser != null && user.getPassword().equals(loginUser.getPassword())) {
                // nickname 매너도수, 알콜도수
                result.put("access-token", jwtUtil.createToken("userId", user.getUserId()));
                result.put("message", SUCCESS);
                result.put("nickname", loginUser.getNickname());
                result.put("mannerPoint", loginUser.getMannerPoint());
                result.put("alcoholPoint", loginUser.getAlcoholPoint());

                HttpSession session = request.getSession();
                session.setAttribute("loginUser", loginUser);

                status = HttpStatus.OK; // 200
            } else {
                result.put("message", FAIL);    //
                status = HttpStatus.NOT_FOUND;  // 404
            }
        } catch (Exception e) {
            result.put("message", FAIL);
            status = HttpStatus.INTERNAL_SERVER_ERROR;  // 500
        }
        return new ResponseEntity<HashMap<String, Object>>(result, status);
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<?> logOut(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return new ResponseEntity<String>(HttpStatus.OK);  // 200
    }
}
