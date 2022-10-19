package com.phoenix.blog.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.blog.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author phoenix
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户id查询当前用户所拥有的角色ids
     *
     * @param id 用户id
     * @return 角色id数组
     */
    List<String> findRoleIdsById(@Param("id") String id);

    /**
     * 通过用户id删除用户角色关系表数据
     *
     * @param userId 用户id
     * @return 是否删除成功
     */
    boolean deleteUserRoleByUserId(@Param("userId") String userId);

    /**
     * 新增用户角色关系 表数据
     *
     * @param userId  用户id
     * @param roleIds 角色id集合
     * @return 是否新增成功
     */
    boolean saveUserRole(@Param("userId") String userId,
                         @Param("roleIds") List<String> roleIds);

}
