package org.travel.dto.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.travel.entity.CompanyProfile;

@Data
@NoArgsConstructor
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
