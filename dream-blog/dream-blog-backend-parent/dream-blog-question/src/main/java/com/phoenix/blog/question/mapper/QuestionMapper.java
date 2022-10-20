package com.phoenix.blog.question.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.phoenix.blog.common.request.UserInfoRequest;
import com.phoenix.blog.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 问题信息表 Mapper 接口
 * </p>
 *
 * @author phoenix
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 根据标签id分页查询问题列表
     *
     * @param page    分页对象
     * @param labelId 标签id
     * @return 问题列表
     */
    IPage<Question> findListByLabelId(
            IPage<Question> page, @Param("labelId") String labelId);

    /**
     * 根据问题id查询问题详情与属性标签ids
     *
     * @param id 问题id
     * @return 带标签列表的问题详情
     */
    Question findQuestionAndLabelIdsById(String id);

    /**
     * 通过问题id删除问题标签中间表
     *
     * @param questionId 问题id
     */
    void deleteQuestionLabel(@Param("questionId") String questionId);

    /**
     * 新增问题标签中间表数据
     *
     * @param questionId 问题id
     * @param labelIds   标签id集合
     */
    void saveQuestionLabel(@Param("questionId") String questionId,
                           @Param("labelIds") List<String> labelIds);

    boolean updateUserInfo(UserInfoRequest userInfoRequest);
}
