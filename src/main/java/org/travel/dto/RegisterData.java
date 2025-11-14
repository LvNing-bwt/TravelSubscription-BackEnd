package org.travel.dto;

import lombok.Builder;
import lombok.Data;
import org.travel.entity.Company;

import java.time.LocalDateTime;

@Data
@Builder
public class RegisterData {
    private Long uid;
    private String username;
    private Company.CompanyStatus companyStatus;
    private LocalDateTime registerTime;
}
