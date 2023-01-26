package com.ssafy.Homezakaya.model.service;

import com.ssafy.Homezakaya.model.dto.RoomDto;

import java.util.List;

public interface RoomService {
    boolean insertRoom(RoomDto room);
    List<RoomDto> selectRooms();
    boolean checkPassword(RoomDto room);
    boolean enterRoom(int roomId);
    boolean quitRoom(int roomId);
    boolean deleteRoom(int roomId);
}