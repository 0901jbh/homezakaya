package com.ssafy.Homezakaya.model.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {
    private int roomId;
    private String title;
    private String password;
    private String category;
    private String hostId;
    private int personLimit;
    private int personCount;
    private boolean isPrivate;
}
