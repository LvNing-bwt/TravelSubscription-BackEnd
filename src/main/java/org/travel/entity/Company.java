package org.travel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

@Data
@TableName("companies")
@Builder
public class Company {
    @TableId(type = IdType.AUTO)
    private Long companyId;
    @TableField("company_name")
    private String companyName;
    @TableField("province_id")
    private String provinceId;
    @TableField("city_id")
    private String cityId;
    @TableField("credit_code")
    private String creditCode;
    @TableField("legal_person_name")
    private String legalPersonName;
    @TableField("company_status")
    private CompanyStatus companyStatus = CompanyStatus.PENDING;

    public enum CompanyStatus{
        PENDING,
        ACTIVE,
        SUSPENDED
    }
}
