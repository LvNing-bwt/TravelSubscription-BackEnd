package org.travel.service;

import lombok.extern.slf4j.Slf4j;
import org.travel.dto.LoginData;
import org.travel.dto.LoginRequest;
import org.travel.entity.CompanyAccount;
import org.travel.exception.LoginException;
import org.travel.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class LoginByUsernameService implements LoginService{

    @Autowired
    private CompanyAccountService companyAccountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public LoginData login(LoginRequest loginRequest) {
        log.info("进来了吗？？？用户名:{}",loginRequest.getUsername());
        CompanyAccount account = companyAccountService.findByUsername(loginRequest.getUsername());
        log.info("查询用户名: {}, 结果: {}", loginRequest.getUsername(), account);

        if(account == null){
            log.info("输入不存在的用户名，进到这里了吗");
            throw LoginException.userNotFound();
        }

        if(!passwordEncoder.matches(loginRequest.getPassword(),account.getPassword())) {
            throw LoginException.passwordError();
        }

        if(account.getStatus() == CompanyAccount.AccountStatus.DISABLED) {
            throw LoginException.accountDisabled();
        }

        String token = JwtUtil.generateToken(account.getUid());

        return LoginData.builder()
                .token(token)
                .loginTime(LocalDateTime.now())
                .build();
    }
}
