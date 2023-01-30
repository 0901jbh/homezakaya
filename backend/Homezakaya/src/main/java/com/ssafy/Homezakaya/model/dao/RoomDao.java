package com.ssafy.Homezakaya.model.dao;

import com.ssafy.Homezakaya.model.dto.RoomDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoomDao {
    int insertRoom(RoomDto room);
    List<RoomDto> selectRooms();
    RoomDto selectRoom(int roomId);
    RoomDto checkPassword(RoomDto room);
    int enterRoom(int roomId);
    int quitRoom(int roomId);
    int deleteRoom(int roomId);
}