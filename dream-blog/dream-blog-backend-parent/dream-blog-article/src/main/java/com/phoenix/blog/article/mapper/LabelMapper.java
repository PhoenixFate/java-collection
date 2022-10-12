package com.phoenix.blog.article.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phoenix.blog.article.request.LabelRequest;
import com.phoenix.blog.entity.Label;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 标签表 Mapper 接口
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@Mapper
public interface LabelMapper extends BaseMapper<Label> {

    /**
     * 如果自定义sql要分页查询，只要在mapper写不带分页功能的sql语句，mybatis-plu会拦截并添加分页条件
     * 但需要满足条件：
     * 第一个参数必须传入分页对象，第二个参数是查询条件,使用@Param取别名
     * 最终会将查询到的数据封装到IPage实现类中
     *
     * @return 分页后的数据
     */
    IPage<Label> queryPage(IPage<Label> page, @Param("labelRequest") LabelRequest labelRequest);

}
