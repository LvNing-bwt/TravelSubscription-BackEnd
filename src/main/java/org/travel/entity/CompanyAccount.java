package org.travel.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("company_account")
public class CompanyAccount {
    @TableId(type = IdType.AUTO)
    private Long uid;
    private String username;
    @TableField("password_hash")
    private String password;
    private String email;
    private String phone;

    private Role role = Role.COMPANY;

    private AccountStatus status = AccountStatus.NORMAL;
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;
    @TableField("login_fail_count")
    private Integer loginFailCount = 0;

    public enum Role{
        COMPANY,
        ADMIN,
        SUPER_ADMIN
    }

    public enum AccountStatus{
        NORMAL((short) 1,"正常"),
        DISABLED((short) 2,"禁用");

        @EnumValue
        private final short code;
        private final String desc;

        AccountStatus(short code,String desc){
            this.code = code;
            this.desc = desc;
        }
    }

    public CompanyAccount(){
    }

    public CompanyAccount(String username,String password){
        this.username = username;
        this.password = password;
    }
}
