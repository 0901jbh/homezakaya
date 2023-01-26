package com.ssafy.Homezakaya.model.service;

import com.ssafy.Homezakaya.model.dao.RoomDao;
import com.ssafy.Homezakaya.model.dto.RoomDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomDao roomDao;

    @Override
    public boolean insertRoom(RoomDto room){
        return roomDao.insertRoom(room) > 0;
    }

    @Override
    public List<RoomDto> getRooms(){
        return roomDao.selectRooms();
    }

    @Override
    public boolean checkPassword(RoomDto room){
        return roomDao.checkPassword(room) > 0;
    }

    @Override
    public boolean enterRoom(int roomId){
        return roomDao.enterRoom(roomId) > 0;
    }

    @Override
    public boolean quitRoom(int roomId){
        return roomDao.quitRoom(roomId) > 0;
    }

    @Override
    public boolean deleteRoom(int roomId){
        return roomDao.deleteRoom(roomId) > 0;
    }
}