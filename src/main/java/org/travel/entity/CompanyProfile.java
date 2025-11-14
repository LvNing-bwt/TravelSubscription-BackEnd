package org.travel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("company_profile")
public class CompanyProfile {
    @TableId
    @TableField("company_id")
    private Long companyId;
    @TableField("contact_person")
    private String contactPerson;
    @TableField("company_phone")
    private String companyPhone;

    @TableField("avatar_url")
    private String avatar;
    @TableField("company_address")
    private String address;
    @TableField("company_intro")
    private String companyInfo;

    @TableField("refresh_days")
    private Integer refreshDays = 7;
    @TableField("business_license_url")
    private String businessLicense;
    @TableField("id_card_front_url")
    private String idCardFront;
    @TableField("id_card_back_url")
    private String idCardBack;

    @TableField("real_name_verified")
    private Boolean realNameVerified = false;


    public CompanyProfile(){

    };

    public CompanyProfile(Long companyId,String contactPerson,String companyPhone){
        this.companyId = companyId;
        this.contactPerson = contactPerson;
        this.companyPhone = companyPhone;
    }
}
