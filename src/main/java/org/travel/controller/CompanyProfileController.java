package org.travel.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.travel.dto.Response;
import org.travel.dto.vo.CompanyProfileVO;
import org.travel.entity.CompanyProfile;
import org.travel.service.CompanyInfoService;
import org.travel.service.UpdateProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/company/profile")
@Slf4j
public class CompanyProfileController {
    @Autowired
    private CompanyInfoService companyInfoService;

    @Autowired
    private UpdateProfileService updateProfileService;

    private Long getCurrentUid() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping
    public Response<CompanyProfileVO> getProfile(){
        Long uid = getCurrentUid();
        CompanyProfileVO result = companyInfoService.getFullInfo(uid);
        return Response.success(result);
    }

    @PatchMapping("/company_phone")
    public Response<Void> updatePhone(@RequestBody String companyPhone){
        Long uid = getCurrentUid();
        updateProfileService.updateCompanyPhone(uid,companyPhone);
        return Response.success(null);
    }


    @PatchMapping("/company_info")
    public Response<Void> updateCompanyInfo(@RequestBody String companyInfo){
        Long uid = getCurrentUid();
        updateProfileService.updateCompanyInfo(uid, companyInfo);
        return Response.success(null);
    }

    @PatchMapping("/company_address")
    public Response<Void> updateCompanyAddress(@RequestBody String companyAddress){
        Long uid = getCurrentUid();
        updateProfileService.updateCompanyAddress(uid, companyAddress);
        return Response.success(null);
    }

    @PatchMapping("/refresh_days")
    public Response<Void> updateRefreshDays(@RequestBody Integer refreshDays){
        Long uid = getCurrentUid();
        updateProfileService.updateRefreshDays(uid, refreshDays);
        return Response.success(null);
    }

    @PatchMapping("/contact_person")
    public Response<Void> updateContactPerson(@RequestBody String contactPerson){
        Long uid = getCurrentUid();
        updateProfileService.updateContactPerson(uid,contactPerson);
        return Response.success(null);
    }
}
