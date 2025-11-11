package org.travel.controller;

import jakarta.validation.Valid;
import org.travel.dto.*;
import org.travel.service.LoginService;
import org.travel.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RegisterService registerService;

    @PostMapping("/login")
    public Response<LoginData> login(@Valid @RequestBody LoginRequest loginRequest){
        LoginData result = loginService.login(loginRequest);
        return Response.success(result);
    }

    @PostMapping("/register")
    public Response<RegisterData> register(@Valid @RequestBody RegisterRequest registerRequest){
        RegisterData result = registerService.register(registerRequest);
        return Response.success(result);
    }
}
