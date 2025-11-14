package org.travel.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.travel.dto.*;
import org.travel.service.LoginService;
import org.travel.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RegisterService registerService;


    private Long getCurrentUid() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping("/login")
    public Response<LoginData> login(@Valid @RequestBody LoginRequest loginRequest){
        LoginData result = loginService.login(loginRequest);
        return Response.success(result);
    }

    @PostMapping("/register")
    public Response<RegisterData> register(@Valid @RequestBody RegisterRequest registerRequest){
        log.info("什么情况？");
        RegisterData result = registerService.register(registerRequest);
        return Response.success(result);
    }

    @GetMapping("/auto-login")
    public Response<Void> autoLogin() {
        Long uid = getCurrentUid();
        loginService.autoLogin(uid);
        return Response.success(null);
    }
}
