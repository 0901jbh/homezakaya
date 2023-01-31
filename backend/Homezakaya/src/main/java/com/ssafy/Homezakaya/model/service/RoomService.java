package com.ssafy.Homezakaya.model.service;

import com.ssafy.Homezakaya.model.dto.RoomDto;

import java.util.List;

public interface RoomService {
    int createRoom(RoomDto room);
    List<RoomDto> getRooms();
    RoomDto getRoom(int roomId);
    String getPassword(int roomId);
    boolean enterRoom(int roomId);
    boolean quitRoom(int roomId);
    boolean removeRoom(int roomId);
}