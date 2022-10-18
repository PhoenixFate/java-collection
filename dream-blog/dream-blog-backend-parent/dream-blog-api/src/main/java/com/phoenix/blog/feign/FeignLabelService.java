package com.phoenix.blog.feign;

import com.phoenix.blog.common.constant.DreamBlogServerNameConstant;
import com.phoenix.blog.entity.Label;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Author phoenix
 * @Date 10/19/22 00:09
 * @Version 1.0
 */
//@@FeignClient value:目标微服务的名称, path:目标微服务的上下文路径
@FeignClient(value = DreamBlogServerNameConstant.DREAM_BLOG_ARTICLE, path = "/article")
public interface FeignLabelService {

    @GetMapping("/label/list/{ids}")
    List<Label> getLabelListByIds(@PathVariable("ids") List<String> labelIds);

}
