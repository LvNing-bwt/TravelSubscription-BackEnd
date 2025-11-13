package org.travel.service;

import org.travel.dto.LoginData;
import org.travel.dto.LoginRequest;


public interface LoginService {
    public LoginData login(LoginRequest loginRequest);
    public void autoLogin(Long uid);
}
