package me.hearta.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LoginData {
    private String token; //JWT令牌
    private LocalDateTime loginTime;
}
