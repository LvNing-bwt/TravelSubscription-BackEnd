package me.hearta.service;

import me.hearta.dto.vo.CompanyProfileVO;
import me.hearta.entity.CompanyProfile;
import me.hearta.mapper.CompanyProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyInfoService {

    @Autowired
    private CompanyProfileMapper companyProfileMapper;

    public CompanyProfileVO getFullInfo(Long uid){
        CompanyProfile companyProfile = companyProfileMapper.selectById(uid);
        return CompanyProfileVO.builder()
                .uid(companyProfile.getUid())
                .username(companyProfile.getUsername())
                .nickName(companyProfile.getNickName())
                .gender(companyProfile.getGender())
                .phone(companyProfile.getPhone())
                .companyName(companyProfile.getCompanyName())
                .companyAddress(companyProfile.getAddress())
                .companyInfo(companyProfile.getCompanyInfo())
                .refreshDays(companyProfile.getRefreshDays())
                .build();
    }
}
