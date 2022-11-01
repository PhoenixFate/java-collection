package com.phoenix.blog.common.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 请求参数基础类、带分页参数
 *
 * @param <T>
 */
@Accessors(chain = true)
@Data
public class BaseRequest<T> implements Serializable {

    @ApiModelProperty(value = "页码", required = false, example = "1")
    private long current = 1;

    @ApiModelProperty(value = "每页显示多少条", required = false, example = "10")
    private long size = 10;

    /**
     * 封装分页对象
     *
     * @return 分页对象
     */
    @ApiModelProperty(hidden = true) // 不在swagger接口文档中显示
    public IPage<T> getPage() {
        return new Page<T>().setCurrent(this.current).setSize(this.size);
    }

}
