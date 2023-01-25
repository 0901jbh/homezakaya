package com.ssafy.Homezakaya.model.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private String userId;
    private String password;
    private String nickname;
    private String email;
    private String birthDate;
    private String gender;
    private double mannerPoint;
    private double alcoholPoint;
    private String username;
}
