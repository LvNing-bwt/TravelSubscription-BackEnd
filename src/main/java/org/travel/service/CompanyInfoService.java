package org.travel.service;

import org.travel.dto.vo.CompanyProfileVO;
import org.travel.entity.CompanyAccount;
import org.travel.entity.CompanyProfile;
import org.travel.mapper.CompanyProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyInfoService {

    @Autowired
    private CompanyAccountService companyAccountService;

    public CompanyProfileVO getFullInfo(Long uid){
        return companyAccountService.findProfileByUid(uid);
    }
}
