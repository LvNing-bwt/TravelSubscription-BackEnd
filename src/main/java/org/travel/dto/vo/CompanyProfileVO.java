package org.travel.dto.vo;

import lombok.Builder;
import lombok.Data;
import org.travel.entity.CompanyProfile;

@Data
@Builder
public class CompanyProfileVO {
    private Long uid;
    private String username;
    private String nickName;
    private CompanyProfile.Gender gender;
    private String phone;
    private String companyName;
    private String companyAddress;
    private String companyInfo;
    private Integer refreshDays;
}
