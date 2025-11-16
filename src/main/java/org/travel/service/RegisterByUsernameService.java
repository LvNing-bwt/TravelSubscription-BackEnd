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
import org.springframework.dao.DuplicateKeyException;
import org.travel.config.OpenfireConfig;
import org.travel.dto.RegisterData;
import org.travel.dto.RegisterRequest;
import org.travel.dto.Response;
import org.travel.entity.*;
import org.travel.exception.BusinessException;
import org.travel.exception.RegisterException;
import org.travel.exception.SystemConfigException;
import org.travel.mapper.CompanyMapper;
import org.travel.mapper.CompanyProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.travel.mapper.PubSubNodeMapper;
import org.travel.mapper.SubscriptionTierMapper;

import java.time.LocalDateTime;

@Service
@Slf4j
public class RegisterByUsernameService implements RegisterService{

    @Autowired
    private CompanyAccountService companyAccountService;

    @Autowired
    private CompanyProfileMapper companyProfileMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private SubscriptionTierMapper subscriptionTierMapper;

    @Autowired
    private PubSubNodeService pubSubNodeService;

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

        // Company表
        log.info("01-进入save Company表的部分");
        Company newCompany = Company.builder()
                .companyName(registerRequest.getCompanyName())
                .provinceId(registerRequest.getProvinceId())
                .cityId(registerRequest.getCityId())
                .creditCode(registerRequest.getCreditCode())
                .legalPersonName(registerRequest.getLegalPersonName())
                .build();
        try {
            companyMapper.insert(newCompany);
        } catch (DuplicateKeyException e) {
            // 解析是哪个字段重复了
            String errorMsg = e.getMessage();
            if (errorMsg.contains("credit_code")) {
                throw RegisterException.companyCreditCodeExists(registerRequest.getCreditCode());
            } else if (errorMsg.contains("uk_company_city")) {
                throw RegisterException.companyNameExists(registerRequest.getCompanyName());
            } else {
                throw new RegisterException();
            }
        }

        createCompanyNode(newCompany);

        log.info("03-进入subscription_tiers表的部分");
        SubscriptionTier subscriptionTier = new SubscriptionTier(newCompany.getCompanyId());
        subscriptionTierMapper.insert(subscriptionTier);

        // 账号
        CompanyAccount newAccount = new CompanyAccount(
                registerRequest.getUsername(),
                encodedPassword,
                newCompany.getCompanyId()
        );

        Long uid;
        try {
            log.info("04-进入save companyProfile表的部分");
            companyAccountService.save(newAccount);
            uid = newAccount.getUid();
            log.info("ui为{}",uid);
            log.info("username为{}",registerRequest.getUsername());
            CompanyProfile newProfile = new CompanyProfile(newAccount.getCompanyId(),
                    registerRequest.getContactPerson() ,
                    registerRequest.getCompanyPhone());
            companyProfileMapper.insert(newProfile);
        }catch (DataAccessException e){
            log.error("数据库操作失败 - 具体异常:", e);
            throw RegisterException.databaseSaveFailed();
        }

        xmppRegister(uid,registerRequest.getPassword());

        return RegisterData.builder()
                .username(registerRequest.getUsername())
                .uid(uid)
                .companyStatus(Company.CompanyStatus.PENDING)
                .registerTime(LocalDateTime.now())
                .build();
    }

    private void createCompanyNode(Company company){
        log.info("02-进入save PubSubNode表的部分");
        PubSubNode pubSubNode = PubSubNode.builder()
                .nodeName(company.getCompanyName())
                .nodeType(PubSubNode.NodeType.COMPANY)
                .companyId(company.getCompanyId())
                .parentNodeId(findParentCityNodeId(company.getCityId()))
                .active(false)
                .displayOrder(0)
                .build();

        pubSubNodeService.save(pubSubNode);
    }

    private Long findParentCityNodeId(String cityId){
        PubSubNode cityNode = pubSubNodeService.findByLocationId(cityId);
        if(cityNode == null) {
            log.error("城市节点未初始化:{}",cityId);
            throw new SystemConfigException("城市节点未初始化：" + cityId);
        }
        return cityNode.getNodeId();
    }

    private void xmppRegister(Long uid,String password){
        log.info("05-进入xmpp注册部分");
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
