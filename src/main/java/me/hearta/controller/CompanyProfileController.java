package me.hearta.controller;

import lombok.extern.slf4j.Slf4j;
import me.hearta.dto.Response;
import me.hearta.dto.vo.CompanyProfileVO;
import me.hearta.service.CompanyInfoService;
import me.hearta.service.UpdateProfileService;
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
