package org.travel.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("company_profile")
public class CompanyProfile {
    @TableId
    private Long uid;
    private String username;
    @TableField("company_name")
    private String companyName;
    @TableField("nickname")
    private String nickName;
    private String phone;
    @TableField("legal_person_gender")
    private Gender gender = Gender.UNKNOWN;

    @TableField("avatar_url")
    private String avatar;
    @TableField("company_address")
    private String address;
    @TableField("company_intro")
    private String companyInfo;

    @TableField("refresh_days")
    private Integer refreshDays;

    @TableField("business_license_url")
    private String businessLicense;
    @TableField("id_card_front_url")
    private String idCardFront;
    @TableField("id_card_back_url")
    private String idCardBack;

    @TableField("real_name_verified")
    private Boolean realNameVerified = false;

    public enum Gender{
        MALE,
        FEMALE,
        UNKNOWN
    };

    public CompanyProfile(){

    };

    public CompanyProfile(Long uid,String username){
        this.uid = uid;
        this.username = username;
    }
}
