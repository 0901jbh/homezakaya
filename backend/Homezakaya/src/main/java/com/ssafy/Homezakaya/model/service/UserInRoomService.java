package com.ssafy.Homezakaya.model.service;

import com.ssafy.Homezakaya.model.dto.UserInRoomDto;

public interface UserInRoomService {
<<<<<<< HEAD
    boolean insertUserInRoom(UserInRoomDto userInRoom);
    boolean deleteUserInRoom(String userId);
    Integer getRoomId(String userId);
=======
    boolean createUserInRoom(UserInRoomDto userInRoom);
    boolean removeUserInRoom(String userId);
    int getRoomId(String userId);
>>>>>>> 8c125aa0904858b9495cd38f2a8e42b04ebe82c4
}