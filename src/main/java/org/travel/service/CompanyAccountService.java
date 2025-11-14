package org.travel.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.toolkit.MPJWrappers;
import org.travel.dto.vo.CompanyProfileVO;
import org.travel.entity.Company;
import org.travel.entity.CompanyAccount;
import org.travel.entity.CompanyProfile;
import org.travel.mapper.CompanyAccountMapper;
import org.springframework.stereotype.Service;

@Service
public class CompanyAccountService extends ServiceImpl<CompanyAccountMapper, CompanyAccount> {
    public CompanyAccount findByUsername(String username){
        return this.lambdaQuery()  // 1. 创建Lambda查询构造器
                .eq(CompanyAccount::getUsername,username) // 2. 设置相等条件
                .one(); // 3. 执行查询并返回单个结果
    }

    public CompanyProfileVO findProfileByUid(Long uid){
        return baseMapper.selectJoinOne(
                CompanyProfileVO.class,
                MPJWrappers.<CompanyAccount>lambdaJoin()
                        .select(CompanyAccount::getUid,
                                CompanyAccount::getUsername)
                        .select(CompanyProfile::getContactPerson,
                                CompanyProfile::getCompanyPhone,
                                CompanyProfile::getAddress,
                                CompanyProfile::getCompanyInfo,
                                CompanyProfile::getRefreshDays)
                        .select(Company::getCompanyName)
                        .selectAs(Company::getCreditCode, "credit_code")
                        .leftJoin(Company.class,Company::getCompanyId,CompanyAccount::getCompanyId)
                        .leftJoin(CompanyProfile.class,CompanyProfile::getCompanyId,Company::getCompanyId)
                        .eq(CompanyAccount::getUid,uid)
                );
    }
}
