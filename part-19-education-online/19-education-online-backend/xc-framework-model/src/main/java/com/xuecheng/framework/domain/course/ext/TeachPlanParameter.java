package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.TeachPlan;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Created by admin on 2018/2/7.
 */
@Data
@ToString
public class TeachPlanParameter extends TeachPlan {

    //二级分类ids
    List<String> bIds;
    //三级分类ids
    List<String> cIds;

}
