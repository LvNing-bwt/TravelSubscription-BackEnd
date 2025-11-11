package org.travel.controller;

import lombok.extern.slf4j.Slf4j;
import org.travel.dto.Response;
import org.travel.dto.vo.CompanyProfileVO;
import org.travel.service.CompanyInfoService;
import org.travel.service.UpdateProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company/profile")
@Slf4j
public class CompanyProfileController {
    @Autowired
    private CompanyInfoService companyInfoService;

    @Autowired
    private UpdateProfileService updateProfileService;

    @GetMapping
    public Response<CompanyProfileVO> getProfile(){
        Long uid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CompanyProfileVO result = companyInfoService.getFullInfo(uid);
        return Response.success(result);
    }

    @PatchMapping("/nickname")
    public Response<Void> updateNickname(String nickname){
        Long uid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        updateProfileService.updateNickname(uid, nickname);
        return Response.success(null);
    }
}
