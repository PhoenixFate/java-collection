package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = {"文件管理接口"})
public interface FileSystemControllerApi {

    @ApiOperation("上传文件接口")
    UploadFileResult uploadFile(MultipartFile multipartFile, String fileTag, String businessKey, String metaData);

}
