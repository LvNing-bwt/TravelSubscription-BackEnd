package org.travel.dto.vo;

import lombok.Builder;
import lombok.Data;
import org.travel.entity.CompanyProfile;

@Data
@Builder
public class CompanyProfileVO {
    private Long uid;
    private String username;
    private String contactPerson;
    private String companyPhone;
    private String companyName;
    private String creditCode;
    private String companyAddress;
    private String companyInfo;
    private Integer refreshDays;
}
