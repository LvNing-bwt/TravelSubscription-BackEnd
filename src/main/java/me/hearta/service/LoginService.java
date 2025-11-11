package me.hearta.service;

import me.hearta.dto.LoginData;
import me.hearta.dto.LoginRequest;


public interface LoginService {
    public LoginData login(LoginRequest loginRequest);
}
