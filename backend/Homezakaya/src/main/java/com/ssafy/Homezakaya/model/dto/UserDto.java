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
public class UserDto {
    private String userId;
    private String password;
    private String nickname;
    private String email;
    private double mannerPoint;
    private int evaluatedCount;
    private double alcoholPoint;
    private String refreshToken;
    private String state;
}
