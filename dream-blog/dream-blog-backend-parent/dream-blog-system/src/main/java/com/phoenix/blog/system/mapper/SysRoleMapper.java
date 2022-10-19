package com.phoenix.blog.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.blog.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author phoenix
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 通过角色id删除角色菜单关系表数据
     *
     * @param roleId 角色id
     * @return 是否删除成功
     */
    boolean deleteRoleMenuByRoleId(@Param("roleId") String roleId);

    /**
     * 根据角色id查询此角色拥有的权限菜单ids
     *
     * @param id 角色id
     * @return 菜单id集合
     */
    List<String> findMenuIdsById(@Param("id") String id);

    /**
     * 新增角色菜单权限数据到 sys_role_menu
     *
     * @param roleId  角色id
     * @param menuIds 菜单 id 集合
     * @return 是否保存成功
     */
    boolean saveRoleMenu(@Param("roleId") String roleId, @Param("menuIds") List<String> menuIds);
}
