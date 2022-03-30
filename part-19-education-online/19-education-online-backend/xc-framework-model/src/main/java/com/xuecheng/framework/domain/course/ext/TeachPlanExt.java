package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.TeachPlan;
import lombok.Data;
import lombok.ToString;

/**
 * Created by admin on 2018/2/7.
 */
@Data
@ToString
public class TeachPlanExt extends TeachPlan {

    //媒资文件id
    private String mediaId;

    //媒资文件原始名称
    private String mediaFileOriginalName;

    //媒资文件访问地址
    private String mediaUrl;
}
