package com.phoenix.blog.common.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @Author phoenix
 * @Date 10/16/22 14:49
 * @Version 1.0
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "phoenix.blog")
public class BlogProperty implements Serializable {

    //会将application.yml中phoenix.blog.alibabaCloud下的属性拷贝到当前类中
    private AlibabaCloudProperty alibabaCloud;

}
