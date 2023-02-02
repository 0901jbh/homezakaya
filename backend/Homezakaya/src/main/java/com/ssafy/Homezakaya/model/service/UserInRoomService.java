package com.ssafy.Homezakaya.model.service;

import com.ssafy.Homezakaya.model.dto.UserInRoomDto;

public interface UserInRoomService {
    boolean createUserInRoom(UserInRoomDto userInRoom);
    boolean removeUserInRoom(String userId);
    Integer getRoomId(String userId);
}