package com.ssafy.Homezakaya.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String userId;
    private String password;
    private String nickname;
    private String email;
    private Date birthDate;
    private String gender;
    private double mannerPoint;
    private double alcoholPoint;
    private String username;
}
