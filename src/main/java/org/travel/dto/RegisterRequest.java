package org.travel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度3-20位")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度6-20位")
    private String password;

    // =============== 公司信息 ================
    @NotBlank(message = "公司名不能为空")
    private String companyName;

    @NotBlank(message = "统一社会信用代码不能为空")
    @Pattern(regexp = "^[0-9A-Z]{18}$",message = "信用代码格式不正确")
    private String creditCode;

    @NotBlank(message = "省份不能为空")
    private String provinceId; //行政区划代码

    @NotBlank(message = "城市不能为空")
    private String cityId;

    @NotBlank(message = "法人姓名不能为空")
    private String legalPersonName;

    // ================== 可选信息 ==============
    private String contactPerson;
    private String companyPhone;
}
