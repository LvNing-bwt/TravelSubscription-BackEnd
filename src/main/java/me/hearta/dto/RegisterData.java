package me.hearta.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RegisterData {
    private Long uid;
    private String username;
    private LocalDateTime registerTime;
}
