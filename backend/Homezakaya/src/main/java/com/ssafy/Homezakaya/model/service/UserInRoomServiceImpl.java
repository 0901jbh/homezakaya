package com.ssafy.Homezakaya.model.service;

import com.ssafy.Homezakaya.model.dao.UserInRoomDao;
import com.ssafy.Homezakaya.model.dto.UserInRoomDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInRoomServiceImpl implements UserInRoomService {

    @Autowired
    private UserInRoomDao userInRoomDao;

    @Override
    public boolean insertUserInRoom(UserInRoomDto userInRoom) {
        return userInRoomDao.insertUserInRoom(userInRoom) > 0;
    }

    @Override
    public boolean deleteUserInRoom(String userId) {
        return userInRoomDao.deleteUserInRoom(userId) > 0;
    }

    @Override
    public int getRoomId(String userId) {
        return userInRoomDao.selectRoomIdByUserId(userId);
    }
}