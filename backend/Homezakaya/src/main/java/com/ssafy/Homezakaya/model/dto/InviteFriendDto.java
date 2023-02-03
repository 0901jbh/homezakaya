package com.ssafy.Homezakaya.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InviteFriendDto {
    private String fromUserId;
    private String toUserId;
}
