package com.ssafy.Homezakaya.model.service;

import com.ssafy.Homezakaya.model.dto.RoomDto;

import java.util.List;

public interface RoomService {
    boolean createRoom(RoomDto room);
    List<RoomDto> getRooms();
    RoomDto checkPassword(RoomDto room);
    boolean enterRoom(int roomId);
    boolean quitRoom(int roomId);
    boolean removeRoom(int roomId);
}