package org.travel.service;

import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.travel.config.OpenfireConfig;
import org.travel.dto.LoginData;
import org.travel.dto.LoginRequest;
import org.travel.entity.CompanyAccount;
import org.travel.exception.LoginException;
import org.travel.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
public class LoginByUsernameService implements LoginService{

    @Autowired
    private CompanyAccountService companyAccountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private XMPPService xmppService;

    @Override
    public LoginData login(LoginRequest loginRequest) {
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

        xmppService.xmppLogin(account.getUid(), loginRequest.getPassword());

        return LoginData.builder()
                .token(token)
                .loginTime(LocalDateTime.now())
                .build();
    }

    @Override
    public void autoLogin(Long uid) {
        CompanyAccount account = companyAccountService.getById(uid);
        if(account == null) throw LoginException.userNotFound();
        if(account.getStatus()!= CompanyAccount.AccountStatus.NORMAL)
            throw LoginException.accountDisabled();
    }

    @Override
    public void logout(Long uid) {
        xmppService.xmppLogout(uid);
    }
}
