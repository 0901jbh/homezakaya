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
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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
        } else {
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

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<HashMap<String, Object>> login(@RequestBody UserDto user, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        HashMap<String, Object> result = new HashMap<>();
        HttpStatus status = null;

        UserDto loginUser = userService.getUser(user.getUserId());

        if (loginUser != null && user.getPassword().equals(loginUser.getPassword())) {
            // 인증 성공 시 accessToken, refreshToken 생성
            String accessToken = jwtUtil.createAccessToken("userInfo", user);
            String refreshToken = jwtUtil.createRefreshToken("userInfo", user);

            // 토큰 정보 전달
            result.put("access-token", accessToken);

            // 토큰 정보 저장
            loginUser.setRefreshToken(refreshToken);
            userService.addTokenInfo(loginUser);
//            Map<String, Object> info = jwtUtil.checkAndGetClaims(accessToken);

            status = HttpStatus.OK; // 200
        } else {
            result.put("message", "로그인 실패");
            status = HttpStatus.UNAUTHORIZED;  // 401
        }

        return new ResponseEntity<HashMap<String, Object>>(result, status);
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<?> logOut(@RequestParam String userId) throws Exception {
        HashMap<String, Object> result = new HashMap<>();
        // DB에서 refresh token 삭제
        // session 정보 삭제 - front
        UserDto loginUser = userService.getUser(userId);
        loginUser.setRefreshToken("");
        userService.removeTokenInfo(loginUser);

        result.put("message", "로그아웃 성공");
        return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);  // 200
    }

    // 토큰 갱신 (session에서 정보 받아와야 함)
    @PostMapping("/refresh") // api check
    public ResponseEntity<?> refresh(@RequestBody UserDto user, HttpServletResponse response) throws Exception {
        HashMap<String, Object> result = new HashMap<>();

        // refresh token 유효성 검사
        jwtUtil.checkAndGetClaims(user.getRefreshToken());

        if (user.getRefreshToken().equals(userService.getUser(user.getUserId()).getRefreshToken())) {
            // 새로운 토큰 발급 및 배포
            String accessToken = jwtUtil.createAccessToken("userInfo", user);
            result.put("access-token", accessToken);
            // 유효성 검사
            jwtUtil.checkAndGetClaims(accessToken);

//            Map<String, Object> info = jwtUtil.checkAndGetClaims(accessToken);
//            result.putAll(info);
        }
        return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);  // 200
    }
}