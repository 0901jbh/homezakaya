package com.ssafy.Homezakaya.model.dao;

import com.ssafy.Homezakaya.model.dto.UserInRoomDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInRoomDao {
    int insertUserInRoom(UserInRoomDto userInRoom);
    int deleteUserInRoom(String userId);
    Integer selectRoomIdByUserId(String userId);
}