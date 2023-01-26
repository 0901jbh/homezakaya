package com.ssafy.Homezakaya.model.service;

import com.ssafy.Homezakaya.model.dto.UserInRoomDto;

public interface UserInRoomService {
    boolean insertUserInRoom(UserInRoomDto userInRoom);
    boolean deleteUserInRoom(String userId);
    int getRoomId(String userId);
}