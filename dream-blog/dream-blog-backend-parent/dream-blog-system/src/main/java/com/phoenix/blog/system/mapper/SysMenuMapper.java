package com.phoenix.blog.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.blog.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单信息表 Mapper 接口
 * </p>
 *
 * @author phoenix
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 查询指定用户id所拥有的菜单权限（目录、菜单、按钮）
     *
     * @param userId 用户id
     * @return 菜单列表
     */
    List<SysMenu> findByUserId(@Param("userId") String userId);
}
