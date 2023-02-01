package com.ssafy.Homezakaya.model.service;

import com.ssafy.Homezakaya.model.dto.FriendDto;
import com.ssafy.Homezakaya.model.dto.UserDto;

import java.sql.SQLException;
import java.util.List;

public interface FriendService {
    // 해당 유저의 친구 목록 조회
    public List<UserDto> getFriendsById(String userId);
    // 친구 삭제
    public int removeFriend(FriendDto friendDto) throws SQLException;
    // 친구 요청
    public int createFriend(FriendDto friendDto) throws SQLException;
    // 친구 요청 리스트 조회
    public List<UserDto> getFriendReqById(String userId) throws SQLException;
    // 친구 요청 수락
    public int modifyFriend(FriendDto friendDto) throws SQLException;
    // 친구 요청 거절/취소
    public int removeFriendReq(FriendDto friendDto) throws SQLException;
    // 유저 조회(검색)
    public List<UserDto> getUserByKeyword(String keyword) throws SQLException;
}
