package com.xuecheng.manage_media.controller;

import com.xuecheng.api.media.MediaUploadControllerApi;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.service.MediaUploadService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/6/6 15:11
 */
@RestController
@RequestMapping("/media/upload")
@AllArgsConstructor
public class MediaUploadController implements MediaUploadControllerApi {

    private MediaUploadService mediaUploadService;

    @PostMapping("/register")
    @Override
    public ResponseResult register(String fileMD5, String fileName, Long fileSize, String mimeType, String fileExtension) {
        return mediaUploadService.register(fileMD5, fileName, fileSize, mimeType, fileExtension);
    }

    @PostMapping("/check/chunk")
    @Override
    public CheckChunkResult checkChunk(String fileMD5, Integer chunkIndex, Long chunkSize) {
        return mediaUploadService.checkChunk(fileMD5, chunkIndex, chunkSize);
    }

    @PostMapping("/upload/chunk")
    @Override
    public ResponseResult uploadChunk(String fileMD5, Integer chunkIndex, MultipartFile file) throws IOException {
        return mediaUploadService.uploadChunk(fileMD5,chunkIndex,file);
    }

    @PostMapping("/merge/chunk")
    @Override
    public ResponseResult mergeChunk(String fileMD5, String fileName, Long fileSize, String mimeType, String fileExtension) {
        return mediaUploadService.mergeChunk(fileMD5,fileName,fileSize,mimeType,fileExtension);
    }
}
