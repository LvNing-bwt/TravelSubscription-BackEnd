package org.travel.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.travel.entity.CompanyAccount;
import org.travel.entity.CompanyProfile;
import org.travel.mapper.CompanyProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@Slf4j
public class UpdateProfileService {
    @Autowired
    private CompanyProfileMapper companyProfileMapper;

    @Autowired
    private CompanyAccountService companyAccountService;

    private void updateProfile(Long uid, Consumer<LambdaUpdateWrapper<CompanyProfile>> updateConsumer){
        CompanyAccount account = companyAccountService.getById(uid);
        LambdaUpdateWrapper<CompanyProfile> lambdaWrapper = new LambdaUpdateWrapper<>();
        lambdaWrapper.eq(CompanyProfile::getCompanyId,account.getCompanyId());
        updateConsumer.accept(lambdaWrapper);
        companyProfileMapper.update(null,lambdaWrapper);
    }

    public void updateCompanyPhone(Long uid,String companyPhone){
        updateProfile(uid,wrapper
                -> wrapper.set(CompanyProfile::getCompanyPhone,companyPhone));
    }

    public void updateCompanyAddress(Long uid,String companyAddress){
        updateProfile(uid,wrapper
                -> wrapper.set(CompanyProfile::getAddress,companyAddress));
    }

    public void updateCompanyInfo(Long uid,String companyInfo){
        updateProfile(uid,wrapper
                -> wrapper.set(CompanyProfile::getCompanyInfo,companyInfo));
    }

    public void updateRefreshDays(Long uid,Integer refreshDays){
        updateProfile(uid,wrapper
                -> wrapper.set(CompanyProfile::getRefreshDays,refreshDays));
    }

    public void updateContactPerson(Long uid,String contactPerson){
        updateProfile(uid,wrapper
                -> wrapper.set(CompanyProfile::getContactPerson,contactPerson));
    }
}
