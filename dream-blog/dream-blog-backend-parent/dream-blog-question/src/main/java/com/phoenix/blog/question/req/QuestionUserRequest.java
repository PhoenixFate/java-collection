package com.phoenix.blog.question.req;

import com.phoenix.blog.common.base.BaseRequest;
import com.phoenix.blog.entity.Question;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 关于查询个人问题的请求类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "QuestionUserRequest", description = "获取指定用户问题的查询条件")
public class QuestionUserRequest extends BaseRequest<Question> {

    @ApiModelProperty(value = "用户ID")
    private String userId;

}
