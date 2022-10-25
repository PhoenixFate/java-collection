package com.phoenix.blog.article.api;

import com.phoenix.blog.article.service.ILabelService;
import com.phoenix.blog.entity.Label;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author phoenix
 * @Date 10/13/22 00:39
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/label")
@AllArgsConstructor
@Api(tags = "标签API接口", description = "不需要通过身份认证就可以直接访问")
public class ApiLabelController {

    private final ILabelService labelService;

    /**
     * 根据标签ids查询对应的标签信息
     *
     * @param ids 标签id数组
     * @return 标签数组
     */
    //allowMultiple = true 表示是数组格式的参数
    @ApiImplicitParam(allowMultiple = true, dataType = "String", name = "ids",
            value = "标签id集合", required = true)
    @ApiOperation("Feign接口--根据标签ids查询对应的标签信息")
    @GetMapping("/list/{ids}")
    public List<Label> getLabelListByIds(@PathVariable("ids") List<String> ids) {
        return labelService.getLabelListByIds(ids);
    }
}
