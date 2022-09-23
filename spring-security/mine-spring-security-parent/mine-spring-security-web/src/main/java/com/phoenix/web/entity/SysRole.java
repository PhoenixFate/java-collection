package com.phoenix.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Auther: phoenix
 */
@Data
public class SysRole implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色描述
     */
    private String remark;

    private Date createDate;

    private Date updateDate;

    /**
     * 存储当前角色的权限资源对象集合
     * 修改角色时用到
     */
    @TableField(exist = false)
    private List<SysPermission> permissionList = Lists.newArrayList();
    /**
     * 存储当前角色的权限资源ID集合
     * 修改角色时用到
     */
    @TableField(exist = false)
    private List<Long> permissionIds = Lists.newArrayList();

    public List<Long> getPermissionIds() {
        if (CollectionUtils.isNotEmpty(permissionList)) {
            permissionIds = Lists.newArrayList();
            for (SysPermission per : permissionList) {
                permissionIds.add(per.getId());
            }
        }
        return permissionIds;
    }
}
