package org.travel.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LoginData {
    private String token; //JWT令牌
    private LocalDateTime loginTime;
}
