package com.phoenix.workflow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * @Auther: phoenix
 */
@Data
@ApiModel("用户实体") //swagger model 注解
@TableName("sys_user") //mybatis-plus 实体注解
public class SysUser implements UserDetails {

    /**
     * id
     *
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码需要通过加密后存储，默认：123456")
    private String password;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像")
    private String imageUrl;

    @ApiModelProperty("封装用户权限")
    @TableField(exist = false) // 不是表中字段
    private Set<GrantedAuthority> authorities;

    /**
     * spring security 获取权限列表 用于校验权限
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * 账号是否过期失效
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账号是否被锁定
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 账号密码是否过期
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账号是否可用
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
