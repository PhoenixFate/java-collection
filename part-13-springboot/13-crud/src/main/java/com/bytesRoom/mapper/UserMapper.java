package com.bytesRoom.mapper;

import com.bytesRoom.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 需要注意，这里没有配置mapper接口扫描包，因此我们需要给每一个Mapper接口添加@Mapper注解，才能被识别。
 *
 * @Mapper
 */
@Mapper
public interface UserMapper extends tk.mybatis.mapper.common.Mapper<User> {

}
