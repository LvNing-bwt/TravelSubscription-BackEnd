package org.travel.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
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

    private void updateProfile(Long uid, Consumer<LambdaUpdateWrapper<CompanyProfile>> updateConsumer){
        LambdaUpdateWrapper<CompanyProfile> lambdaWrapper = new LambdaUpdateWrapper<>();
        lambdaWrapper.eq(CompanyProfile::getUid,uid);
        updateConsumer.accept(lambdaWrapper);
        companyProfileMapper.update(null,lambdaWrapper);
    }

    public void updateNickname(Long uid, String nickname){
        updateProfile(uid,wrapper
                -> wrapper.set(CompanyProfile::getNickName,nickname));
    }

    public void updateGender(Long uid, CompanyProfile.Gender gender){
        updateProfile(uid,wrapper
                -> wrapper.set(CompanyProfile::getGender,gender));
    }

    public void updatePhone(Long uid,String phone){
        updateProfile(uid,wrapper
                -> wrapper.set(CompanyProfile::getPhone,phone));
    }

    public void updateCompanyName(Long uid,String companyName){
        updateProfile(uid,wrapper
                -> wrapper.set(CompanyProfile::getCompanyName,companyName));
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
}
