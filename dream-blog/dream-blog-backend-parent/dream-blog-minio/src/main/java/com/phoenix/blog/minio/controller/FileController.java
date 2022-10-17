package com.phoenix.blog.minio.controller;

import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.common.constant.PlatformEnum;
import com.phoenix.blog.common.property.AlibabaCloudProperty;
import com.phoenix.blog.common.property.BlogProperty;
import com.phoenix.blog.common.util.AlibabaCloudOSSUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件接口：
 * 上传、下载、删除文件
 *
 * @Author phoenix
 * @Date 10/16/22 15:43
 * @Version 1.0
 */
@Api(tags = "文件管理接口")
@RequestMapping("/file")
@RestController
@AllArgsConstructor
public class FileController {

    private final BlogProperty blogProperty;

    /**
     * 上传文件到OSS服务器
     *
     * @param file 文件
     * @return 上传路径URL
     */
    @ApiOperation("上传文件到OSS服务器")
    @PostMapping("/upload")
    @ApiImplicitParam(name = "file", value = "上传的文件", dataType = "__file", required = true)
    public Result upload(@RequestParam("file") MultipartFile file) {
        AlibabaCloudProperty alibabaCloud = blogProperty.getAlibabaCloud();
        return AlibabaCloudOSSUtil.uploadFileToOSS(PlatformEnum.ARTICLE, file, alibabaCloud);
    }

    /**
     * 根据文件URL删除在OSS服务器中对应的文件
     *
     * @param fileUrl 文件路径
     * @return 是否删除成功
     */
    @ApiOperation("根据文件URL删除在OSS服务器中对应的文件")
    @DeleteMapping("/delete")
    @ApiImplicitParam(name = "fileUrl", value = "文件URL", required = true, dataType = "String")
    public Result delete(@RequestParam("fileUrl") String fileUrl) {
        return AlibabaCloudOSSUtil.deleteOSSFile(fileUrl, blogProperty.getAlibabaCloud());
    }

}
