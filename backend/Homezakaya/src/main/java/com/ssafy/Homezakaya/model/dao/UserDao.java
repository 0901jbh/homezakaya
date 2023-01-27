package com.ssafy.Homezakaya.model.dao;

import com.ssafy.Homezakaya.model.dto.UserDto;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface UserDao {

    // 회원 정보 등록 (회원가입)
    void insertUser(UserDto user);

    // 회원 정보 조회 (로그인 id 로 나자신 정보 조회)
    UserDto selectUserById(String userId);

    // 아이디, 닉네임 중복체크용 전체 회원 목록
    UserDto selectUserByNickname(String nickname);

    // 회원 정보 수정
    int updateUser(UserDto user);

    // 회원 정보 삭제 - 기존 비밀번호 입력
    int deleteUser(String userId);  // 0,1 로 확인만

    // 매너 도수 누적
    int addMannerPoint(UserDto user);

    // 매너 도수 계산 (평균내기)
    double selectMannerPoint(String userId);

    int selectEvaluateCount(String userId);
}
