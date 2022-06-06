package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/6/6 14:23
 */
@Api(tags = {"媒资管理接口", "提供文件上传、处理的接口"})
public interface MediaUploadControllerApi {

    /**
     * 文件上传前的准备、校验工作
     * 如果文件已经存在则不再上传
     */
    @ApiOperation("上传上传注册")
    ResponseResult register(String fileMD5, String fileName, Long fileSize, String mimeType, String fileExtension);

    @ApiOperation("校验分块文件是否存在")
    CheckChunkResult checkChunk(String fileMD5, Integer chunkIndex, Long chunkSize);

    @ApiOperation("上传分块")
    ResponseResult uploadChunk(String fileMD5, Integer chunkIndex, MultipartFile file) throws IOException;

    @ApiOperation("合并分块")
    ResponseResult mergeChunk(String fileMD5,String fileName,Long fileSize,String mimeType,String fileExtension);


}
