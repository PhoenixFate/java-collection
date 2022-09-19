package com.phoenix.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @Author phoenix
 * @Date 2022/9/19 16:34
 * @Version 1.0.0
 */
@Data
@TableName("sys_user")
public class SysUser implements UserDetails {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;
    /**
     * 密码需要通过加密后存储
     */
    private String password;
    /**
     * 账户是否有效：1 未过期；0 已过期
     * 1 true
     * 0 false
     */
    private Boolean isAccountNonExpired = true;
    /**
     * 账户是否被锁定：1 未锁定；0 已锁定
     * 1 true
     * 0 false
     */
    private Boolean isAccountNonLocked = true;

    private Boolean isCredentialsNonExpired = true;
    /**
     * 账户是否有效：1 有效；0 无效
     * 1 true
     * 0 false
     */
    private Boolean isEnabled;

    private String nickName;

    private String mobile;

    private String email;

    private Date createDate;

    private Date updateDate;

    /**
     * 用户的权限集合
     */
    @TableField(exist = false) //非表中字段
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * 拥有的角色集合
     */
    @TableField(exist = false)
    private List<SysRole> roleList = Lists.newArrayList();
    /**
     * 拥有的的角色id
     */
    @TableField(exist = false)
    private List<Long> roleIds = Lists.newArrayList();

    public List<Long> getRoleIds() {
        if (CollectionUtils.isNotEmpty(roleList)) {
            roleIds = Lists.newArrayList();
            for (SysRole role : roleList) {
                roleIds.add(role.getId());
            }
        }
        return roleIds;
    }

    /**
     * 当前用户封装的权限资源
     */
    @TableField(exist = false)
    private List<SysPermission> permissions = Lists.newArrayList();

}
