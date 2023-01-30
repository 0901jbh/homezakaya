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
import java.util.Map;
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
    public ResponseEntity<?> createUser(@RequestBody UserDto user) {
        Map<String, Object> resultMap = new HashMap<>();
        userService.createUser(user);
        resultMap.put("message", SUCCESS);
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.CREATED);
    }

    // id 중복확인
    @GetMapping("/id/{userId}")
    public ResponseEntity<?> checkId(@PathVariable String userId) {
        Map<String, Object> resultMap = new HashMap<>();
        if (userService.getUser(userId) != null) {
            resultMap.put("message", "중복된 id입니다.");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.CONFLICT); // 409
        }
        resultMap.put("message", SUCCESS);
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);  // 200
    }

    // nickname 중복확인
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<?> checkNickName(@PathVariable String nickname) {
        Map<String, Object> resultMap = new HashMap<>();
        if (userService.checkNickname(nickname) != null) {
            resultMap.put("message", "중복된 닉네임입니다.");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.CONFLICT);
        }
        resultMap.put("message", SUCCESS);
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
    }

    // 회원 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId) {
        Map<String, Object> resultMap = new HashMap<>();
        if (userService.getUser(userId) == null) {
            resultMap.put("message", "존재하지 않는 id입니다.");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<UserDto>(userService.getUser(userId), HttpStatus.OK);
    }

    // 회원 정보 수정
    @PutMapping
    public ResponseEntity<?> modifyUser(@RequestBody UserDto user) {
        Map<String, Object> resultMap = new HashMap<>();
        if (userService.modifyUser(user)) {
            resultMap.put("message", SUCCESS);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
        } else {
            resultMap.put("message", FAIL);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
        }
    }

    // 회원 정보 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> removeUser(@PathVariable String userId) {
        Map<String, Object> resultMap = new HashMap<>();
        if (userService.removeUser(userId)) {
            resultMap.put("message", SUCCESS);
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
        }else{
            resultMap.put("message", "존재하지 않는 Id");
            return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.NOT_FOUND);
        }

    }

    // 매너 도수 갱신
    @PutMapping("/point/{userId}")
    public ResponseEntity<?> averageOfMannerPoint(@PathVariable String userId, @RequestBody UserDto user) {
        Map<String, Object> resultMap = new HashMap<>();
        double mannerPoint = user.getMannerPoint();
        userService.averageOfMannerPoint(userId, mannerPoint);
        resultMap.put("message", SUCCESS);
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
    }

    // 로그인 (jwt 토큰만 생성)
    @PostMapping("/login")
    public ResponseEntity<HashMap<String, Object>> login(@RequestBody UserDto user, HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> result = new HashMap<>();
        HttpStatus status = null;

        UserDto loginUser = userService.getUser(user.getUserId());
        try {
            if (loginUser != null && user.getPassword().equals(loginUser.getPassword())) {
                // nickname 매너도수, 알콜도수 확인
                result.put("access-token", jwtUtil.createToken("userId", user.getUserId()));
                result.put("message", SUCCESS);
                result.put("nickname", loginUser.getNickname());
                result.put("mannerPoint", loginUser.getMannerPoint());
                result.put("alcoholPoint", loginUser.getAlcoholPoint());

                // 세션
                HttpSession session = request.getSession();
                session.setAttribute("loginUser", loginUser);
                session.setAttribute("access-token", jwtUtil.createToken("userId", user.getUserId()));

                status = HttpStatus.OK; // 200
            } else {
                result.put("message", "로그인 실패");    //
                status = HttpStatus.UNAUTHORIZED;  // 401
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
        HashMap<String, Object> result = new HashMap<>();
        if (session != null) {
            session.invalidate();
        }
        return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);  // 200
    }
}