package com.phoenix.valid.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Author phoenix
 * @Date 1/7/23 00:06
 * @Version 1.0
 */
@Data
public class Employee {

    /**
     * 姓名
     */
    @NotBlank(message = "请输入名称")
    @Length(message = "名称不能超过个 {max} 字符", max = 10)
    public String name;

    /**
     * 年龄
     */
    @NotNull(message = "请输入年龄")
    @Range(message = "年龄范围为 {min} 到 {max} 之间", min = 1, max = 100)
    public Integer age;

    @NotEmpty(message = "兴趣爱好不能为空")
    @Size(message = "兴趣选择最多{max}个", max = 3)
    private List<String> hobbyList;

    @Valid
    @NotNull(message = "用户对象不能为空")
    private UserEntity userEntity;

}
