package com.phoenix.blog.common.property;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author phoenix
 * @Date 10/16/22 15:01
 * @Version 1.0
 */
@Getter
@Setter
public class AlibabaCloudProperty implements Serializable {

    /**
     * oss端点信息
     */
    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;
    /**
     * 存储空间名称
     */
    private String bucketName;
    /**
     * bucket名称，访问文件时作为基础url
     */
    private String bucketDomain;

}
