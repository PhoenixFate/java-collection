package com.phoenix.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phoenix.web.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author phoenix
 * @Date 2022/9/19 17:45
 * @Version 1.0.0
 */
@Mapper//可以不用加，因为MybatisPlusConfig中设置了@MapperScan("com.phoenix.web.mapper")
public interface SysUserMapper extends BaseMapper<SysUser> {
}
