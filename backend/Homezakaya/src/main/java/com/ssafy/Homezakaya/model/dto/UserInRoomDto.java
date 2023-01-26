package com.ssafy.Homezakaya.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInRoomDto {
    private String userId;
    private int roomId;
}
