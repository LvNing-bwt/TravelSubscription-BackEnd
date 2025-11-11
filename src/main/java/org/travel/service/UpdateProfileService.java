package org.travel.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.travel.entity.CompanyProfile;
import org.travel.mapper.CompanyProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateProfileService {
    @Autowired
    private CompanyProfileMapper companyProfileMapper;

    public void updateNickname(Long uid, String nickname){
        LambdaUpdateWrapper<CompanyProfile> lambdaWrapper = new LambdaUpdateWrapper<>();
        lambdaWrapper.eq(CompanyProfile::getUid, uid)
                .set(CompanyProfile::getNickName,nickname);
        // 第一个参数 null 表示不更新实体对象本身，只使用 Wrapper 中的 SET 语句
        companyProfileMapper.update(null,lambdaWrapper);
    }
}
