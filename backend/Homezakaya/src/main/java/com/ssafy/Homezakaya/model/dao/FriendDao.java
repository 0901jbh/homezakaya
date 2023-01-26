package com.ssafy.Homezakaya.model.dao;

import com.ssafy.Homezakaya.model.dto.FriendDto;
import com.ssafy.Homezakaya.model.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FriendDao {
    // 해당 유저의 친구 목록 조회
    public List<UserDto> selectFriendsById(String userId);

    // 친구 삭제
    public int deleteFriend(FriendDto friendDto);

    // 친구 요청 추가(insert)
    public int insertFriend(FriendDto friendDto);

    // 친구 요청 수정(update)
    public int updateFriend(FriendDto friendDto);

    // 친구 요청 조회
    public FriendDto selectFriendReq(FriendDto friendDto);

    // 친구 요청 리스트 조회
    public List<UserDto> selectFriendReqById(String userId);

    // 유저 조회(검색)
    public List<UserDto> selectUserByKeyword(String keyword);
}