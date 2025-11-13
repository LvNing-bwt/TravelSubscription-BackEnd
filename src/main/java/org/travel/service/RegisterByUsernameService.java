package org.travel.service;

import lombok.extern.slf4j.Slf4j;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;
import org.travel.config.OpenfireConfig;
import org.travel.dto.RegisterData;
import org.travel.dto.RegisterRequest;
import org.travel.entity.CompanyAccount;
import org.travel.entity.CompanyProfile;
import org.travel.exception.RegisterException;
import org.travel.mapper.CompanyProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class RegisterByUsernameService implements RegisterService{

    @Autowired
    private CompanyAccountService companyAccountService;

    @Autowired
    private CompanyProfileMapper companyProfileMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OpenfireConfig openfireConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterData register(RegisterRequest registerRequest) {
        CompanyAccount account = companyAccountService.findByUsername(registerRequest.getUsername());

        if(account != null){
            throw RegisterException.usernameExists();
        }

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        CompanyAccount newAccount = new CompanyAccount(
                registerRequest.getUsername(),
                encodedPassword
        );

        Long uid;
        try {
            log.info("进入可能出现异常的注册代码");
            companyAccountService.save(newAccount);
            uid = newAccount.getUid();
            log.info("ui为{}",uid);
            log.info("username为{}",registerRequest.getUsername());
            CompanyProfile newProfile = new CompanyProfile(uid, registerRequest.getUsername());
            companyProfileMapper.insert(newProfile);
        }catch (DataAccessException e){
            throw RegisterException.databaseSaveFailed();
        }

        xmppRegister(uid,registerRequest.getPassword());

        return RegisterData.builder()
                .username(registerRequest.getUsername())
                .uid(uid)
                .registerTime(LocalDateTime.now())
                .build();
    }

    private void xmppRegister(Long uid,String password){
        XMPPTCPConnection adminConnection = null;
        try {
            adminConnection = connectAsAdmin();
            Localpart username = Localpart.from("u" + uid);

            // 获取账号管理器
            AccountManager accountManager = AccountManager.getInstance(adminConnection);
            accountManager.sensitiveOperationOverInsecureConnection(true); //允许不安全连接注册

            accountManager.createAccount(username,password);
            log.info("创建账号完毕");
        } catch (Throwable t) {
            log.error("💥 连接失败详情:", t);
            throw RegisterException.xmppRegisterFailed();
        } finally {
            if(adminConnection != null) {
                adminConnection.disconnect();
            }
        }
    }

    private XMPPTCPConnection connectAsAdmin(){
        XMPPTCPConnection adminConnection = null;
        try {
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration
                    .builder()
                    .setHost(openfireConfig.getHost())
                    .setPort(openfireConfig.getPort())
                    .setXmppDomain(openfireConfig.getDomain())
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.valueOf(openfireConfig.getSecurityMode()))
                    .build();

            adminConnection = new XMPPTCPConnection(config);
            adminConnection.connect();

            adminConnection.login(
                    openfireConfig.getAdmin().getUsername(),
                    openfireConfig.getAdmin().getPassword()
            );
            return adminConnection;
        } catch (Exception e) {
            log.error("❌ 管理员连接失败: {}", e.getMessage(), e);
            throw RegisterException.xmppRegisterFailed();
        }
    }
}
