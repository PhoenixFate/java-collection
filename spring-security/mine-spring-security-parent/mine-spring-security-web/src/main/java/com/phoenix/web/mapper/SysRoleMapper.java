package com.phoenix.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phoenix.web.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author phoenix
 * @Date 9/19/22 22:12
 * @Version 1.0
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 分页查询role；第一个参数必须是Page，有了Page参数之后，其他的参数必须用@Param(xxx)来取别名
     * mybatis-plus会自动分页，但需要第一个参数传入Page对象
     *
     * @param page    分页
     * @param sysRole 条件查询
     * @return 分页后的对象
     */
    IPage<SysRole> selectPage(Page<SysRole> page, @Param("role") SysRole sysRole);

    /**
     * 通过角色id删除角色权限表中的所有记录
     *
     * @param roleId 角色id
     * @return 是否删除成功
     */
    boolean deleteRolePermissionByRoleId(@Param("roleId") Long roleId);

    /**
     * 保存角色与权限关系表数据
     *
     * @param roleId        权限id
     * @param permissionIds 角色所有拥有的权限id
     * @return 是否保存成功
     */
    boolean saveRolePermission(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);

    /**
     * 通过用户id查询所有拥有的角色
     *
     * @param userId 用户id
     * @return 角色集合
     */
    List<SysRole> findListByUserId(@Param("userId") Long userId);
}
