package com.phoenix.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phoenix.web.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author phoenix
 * @Date 2022/9/19 17:45
 * @Version 1.0.0
 */
@Mapper//可以不用加，因为MybatisPlusConfig中设置了@MapperScan("com.phoenix.web.mapper")
public interface SysUserMapper extends BaseMapper<SysUser> {

    IPage<SysUser> selectPage(@Param("page") Page<SysUser> page, @Param("user") SysUser user);

    boolean deleteUserRoleByUserId(@Param("userId") Long userId);

    boolean saveUserRole(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
}
