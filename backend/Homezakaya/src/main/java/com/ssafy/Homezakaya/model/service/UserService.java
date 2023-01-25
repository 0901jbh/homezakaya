package com.ssafy.Homezakaya.model.service;

import com.ssafy.Homezakaya.model.dto.UserDto;
import org.apache.catalina.User;

public interface UserService {

    // 회원 정보 등록
    void createUser(UserDto user);

    // 회원 정보 수정
    boolean modifyUser(UserDto user);

    // 회원 정보 조회
    UserDto getUser(String userId);

    // 회원 정보 삭제
    boolean removeUser(String userId);

    // 매너 도수 계산
    double averageOfMannerPoint(String userId);

}
