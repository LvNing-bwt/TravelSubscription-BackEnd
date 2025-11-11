package me.hearta.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.hearta.dto.RegisterData;
import me.hearta.dto.RegisterRequest;
import me.hearta.entity.CompanyAccount;
import me.hearta.entity.CompanyProfile;
import me.hearta.exception.RegisterException;
import me.hearta.mapper.CompanyProfileMapper;
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

        return RegisterData.builder()
                .username(registerRequest.getUsername())
                .uid(uid)
                .registerTime(LocalDateTime.now())
                .build();
    }
}
