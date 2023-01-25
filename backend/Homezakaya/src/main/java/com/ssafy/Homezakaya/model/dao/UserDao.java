package com.ssafy.Homezakaya.model.dao;

import com.ssafy.Homezakaya.model.dto.UserDto;

public interface UserDao {

    // 회원 정보 등록 (회원가입)
    void insertUser(UserDto user);

    // 회원 정보 조회 (로그인 id 로 나자신 정보 조회)
    UserDto selectUserById(String loginUserId);

    // 회원 정보 수정
    int updateUser(UserDto user);

    // 회원 정보 삭제 - 기존 비밀번호 입력
    int deleteUser(String userId);  // 0,1 로 확인만

    // 매너 도수 계산 (평균내기)
    double averageOfMannerPoint(String userId);

}
