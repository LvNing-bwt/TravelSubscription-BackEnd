package me.hearta.service;

import me.hearta.dto.RegisterData;
import me.hearta.dto.RegisterRequest;


public interface RegisterService {
    public RegisterData register(RegisterRequest registerRequest);
}
