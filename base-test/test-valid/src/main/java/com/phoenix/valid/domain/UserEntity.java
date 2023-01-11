package com.phoenix.valid.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * @Author phoenix
 * @Date 1/7/23 01:29
 * @Version 1.0
 */
@Data
public class UserEntity {

    @NotNull(message = "用户id不能为空")
    private Integer id;

    @NotBlank(message = "用户名称不能为空")
    private String userName;

    @Min(value = 1, message = "输入年龄不能小于1")
    @Max(value = 150, message = "输入年龄不能超过150")
    @NotNull(message = "输入年龄不能为空")
    private Integer userAge;

    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 10, message = "密码⻓度⾄少6位但不超过10位!")
    private String userPwd;

    @Email(message = "邮箱格式不正确")
    private String email;

}
