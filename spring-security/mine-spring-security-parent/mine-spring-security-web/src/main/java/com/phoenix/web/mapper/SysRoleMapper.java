package com.phoenix.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phoenix.web.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
     *
     * @param page    分页
     * @param sysRole 条件查询
     * @return 分页后的对象
     */
    IPage<SysRole> selectPage(Page<SysRole> page, @Param("role") SysRole sysRole);

}
