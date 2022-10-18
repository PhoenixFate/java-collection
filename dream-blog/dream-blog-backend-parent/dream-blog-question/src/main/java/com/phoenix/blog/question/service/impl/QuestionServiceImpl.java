package com.phoenix.blog.question.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phoenix.blog.common.base.BaseRequest;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.Label;
import com.phoenix.blog.entity.Question;
import com.phoenix.blog.feign.FeignLabelService;
import com.phoenix.blog.question.mapper.QuestionMapper;
import com.phoenix.blog.question.req.QuestionUserRequest;
import com.phoenix.blog.question.service.IQuestionService;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 问题信息表 服务实现类
 * </p>
 *
 * @author phoenix
 */
@Service
@AllArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    private final FeignLabelService feignLabelService;

    @Override
    public Result findHotList(BaseRequest<Question> req) {
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        // 1. 状态为 1 或 2，不能查询0（已删除）的
        wrapper.in("status", Arrays.asList(1, 2));
        // 2. 按 回复数 降序排列
        wrapper.orderByDesc("reply");
        return Result.ok(baseMapper.selectPage(req.getPage(), wrapper));
    }

    @Override
    public Result findNewList(BaseRequest<Question> req) {
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        // 1. 状态为 1 或 2，不能查询0（已删除）的
        wrapper.in("status", Arrays.asList(1, 2));
        // 按 更新时间 降序排列，
        wrapper.orderByDesc("update_date");
        return Result.ok(baseMapper.selectPage(req.getPage(), wrapper));
    }

    @Override
    public Result findWaitList(BaseRequest<Question> req) {
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        // 1. 状态为 1 或 2，不能查询0（已删除）的
        wrapper.in("status", Arrays.asList(1, 2));
        // 2. 回复数是 0
        wrapper.eq("reply", 0);
        // 3. 按创建时间降序排列
        wrapper.orderByDesc("create_date");
        return Result.ok(baseMapper.selectPage(req.getPage(), wrapper));
    }

    @Override
    public Result findListByLabelId(BaseRequest<Question> req, String labelId) {
        if (StringUtils.isEmpty(labelId)) {
            return Result.ok("标签ID不能为空");
        }
        return Result.ok(baseMapper.findListByLabelId(req.getPage(), labelId));
    }

    @Override
    public Result findById(String id) {
        // 1. 查询问题详情与标签ids
        Question question = baseMapper.findQuestionAndLabelIdsById(id);
        if (question == null) {
            return Result.error("没有问题详情信息");
        }
        if (CollectionUtils.isNotEmpty(question.getLabelIds())) {
            List<Label> labelList = feignLabelService.getLabelListByIds(question.getLabelIds());
            question.setLabelList(labelList);
        }
        return Result.ok(question);
    }

    @Override
    public Result updateViewCount(String id) {
        if (StringUtils.isEmpty(id)) {
            return Result.error("无效操作");
        }
        // 查询问题详情
        Question question = baseMapper.selectById(id);
        if (question == null) {
            return Result.error("问题不存在");
        }
        // 浏览数+1
        question.setViewCount(question.getViewCount() + 1);
        baseMapper.updateById(question);
        return Result.ok();
    }

    @Override
    public Result updateOrSave(Question question) {
        // 1. 如果是更新，删除 mxg_question_lable 问题标签中间表数据，且设置更新时间
        if (StringUtils.isNotEmpty(question.getId())) {
            baseMapper.deleteQuestionLabel(question.getId());
            question.setUpdateDate(new Date());
        }
        // 2. 问题数据新增或更新到 mxg_question 表
        super.saveOrUpdate(question);
        // 3. 问题所属标签保存到 mxg_question_lable 表
        if (CollectionUtils.isNotEmpty(question.getLabelIds())) {
            baseMapper.saveQuestionLabel(question.getId(), question.getLabelIds());
        }
        // 4. 响应当前操作的问题id
        return Result.ok(question.getId());
    }

    @Override
    public Result deleteById(String id) {
        return this.updateStatus(id, 0); // 0 表示删除
    }


    /**
     * 修改问题状态值
     *
     * @param id     问题id
     * @param status 问题状态
     * @return 是否更新成功
     */
    public Result updateStatus(String id, Integer status) {
        // 通过问题id查询问题详情
        Question question = baseMapper.selectById(id);
        // 设置新状态值
        question.setStatus(status);
        question.setUpdateDate(new Date());
        baseMapper.updateById(question);
        return Result.ok();
    }

    @Override
    public Result updateLikesNumber(String id, int count) {
        if (count != -1 && count != 1) {
            return Result.error("无效操作");
        }
        if (StringUtils.isEmpty(id)) {
            return Result.error("无效操作");
        }
        Question question = baseMapper.selectById(id);
        if (question == null) {
            return Result.error("问题不存在");
        }
        if (question.getLikesNumber() <= 0 && count == -1) {
            return Result.error("无效操作");
        }
        question.setLikesNumber(question.getLikesNumber() + count);
        baseMapper.updateById(question);
        return Result.ok();
    }

    @Override
    public Result findListByUserId(QuestionUserRequest req) {
        if (StringUtils.isEmpty(req.getUserId())) {
            return Result.error("无效用户信息");
        }
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        wrapper.in("status", Arrays.asList(1, 2));
        // 根据用户id查询
        wrapper.eq("user_id", req.getUserId());
        // 排序
        wrapper.orderByDesc("update_date");
        return Result.ok(baseMapper.selectPage(req.getPage(), wrapper));
    }

    @Override
    public Result getQuestionTotal() {
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        // 状态不能是删除的（0），应该是 1 和 2
        wrapper.in("status", Arrays.asList(1, 2));
        Integer total = baseMapper.selectCount(wrapper);
        return Result.ok(total);
    }

}
