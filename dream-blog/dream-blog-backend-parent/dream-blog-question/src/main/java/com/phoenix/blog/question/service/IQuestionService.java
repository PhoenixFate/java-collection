package com.phoenix.blog.question.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phoenix.blog.common.base.BaseRequest;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.Question;
import com.phoenix.blog.question.req.QuestionUserRequest;

/**
 * <p>
 * 问题信息表 服务类
 * </p>
 *
 * @author phoenix
 */
public interface IQuestionService extends IService<Question> {

    /**
     * 分页查询热门问答列表
     *
     * @param req 分页参数
     * @return 问题列表
     */
    Result findHotList(BaseRequest<Question> req);

    /**
     * 分页查询最新问答列表
     *
     * @param req 分页参数
     * @return 问答列表
     */
    Result findNewList(BaseRequest<Question> req);

    /**
     * 分页查询等待回答列表
     *
     * @param req 分页参数
     * @return 问题列表
     */
    Result findWaitList(BaseRequest<Question> req);

    /**
     * 根据标签id分页查询问题列表
     *
     * @param req     分页相关的对象
     * @param labelId 标签id
     * @return 问题列表
     */
    Result findListByLabelId(BaseRequest<Question> req, String labelId);

    /**
     * 通过问题id查询详情
     *
     * @param id 问题id
     * @return 问题详情
     */
    Result findById(String id);

    /**
     * 更新问题浏览数
     *
     * @param id 问题id
     * @return 是否更新成功
     */
    Result updateViewCount(String id);

    /**
     * 修改或新增问题数据
     *
     * @param question 问题参数
     * @return 修改或者新增是否成功
     */
    Result updateOrSave(Question question);

    /**
     * 假删除，只是装饰状态更新为 0 ，表示删除
     *
     * @param id 问题id
     * @return 是否删除成功
     */
    Result deleteById(String id);

    /**
     * 更新点赞数
     *
     * @param id    问题id
     * @param count 是1（点赞）或者-1 (取消点赞)，
     * @return 是否更新成功
     */
    Result updateLikesNumber(String id, int count);

    /**
     * 根据用户id查询问题列表
     *
     * @param req 请求参数
     * @return 某用户的问题列表
     */
    Result findListByUserId(QuestionUserRequest req);

    /**
     * 统计提问总记录
     *
     * @return 统计提问总记录
     */
    Result getQuestionTotal();
}
