package com.phoenix.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.web.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author phoenix
 * @Date 9/19/22 23:45
 * @Version 1.0
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    List<SysPermission> selectPermissionByUserId(@Param("userId") Long userId);

}
