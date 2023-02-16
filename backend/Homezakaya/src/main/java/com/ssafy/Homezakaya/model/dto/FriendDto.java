package com.ssafy.Homezakaya.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendDto {
    private String userAId;
    private String userBId;
    private boolean isConnected;
}