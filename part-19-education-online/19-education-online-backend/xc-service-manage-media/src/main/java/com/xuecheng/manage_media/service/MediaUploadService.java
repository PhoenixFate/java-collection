package com.xuecheng.manage_media.service;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/6/6 15:12
 */
@Service
@RequiredArgsConstructor
public class MediaUploadService {

    private MediaFileRepository mediaFileRepository;

    @Value("xc-service-manage-media.upload-location:")
    private String uploadLocation;

    /**
     * 检查文件在磁盘上是否存在
     * 检查文件在MongoDB中是否存在
     * <p>
     * 文件目录规则：
     * 根据文件md5得到文件路径
     * 规则：
     * 一级目录：md5的第一个字符
     * 二级目录：md5的第二个字符
     * 三级目录：md5的全称
     * 文件名： md5+文件文件拓展名
     *
     * @param fileMD5       fileMD5
     * @param fileName      fileName
     * @param fileSize      fileSize
     * @param mimeType      mimeType
     * @param fileExtension fileExtension
     * @return ResponseResult
     */
    public ResponseResult register(String fileMD5, String fileName, Long fileSize, String mimeType, String fileExtension) {
        //1.检查文件在磁盘上是否存在
        //文件所属目录
        String fileFolderPath = this.getFileFolderPath(fileMD5);
        //文件的路径
        String filePath = this.getFilePath(fileMD5, fileExtension);
        File file = new File(filePath);
        //文件是否存在
        boolean exists = file.exists();

        //2.检查文件信息在MongoDB中是否存在
        Optional<MediaFile> optionalMediaFile = mediaFileRepository.findById(fileMD5);
        if (exists && optionalMediaFile.isPresent()) {
            //文件已经存在
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }
        //文件不存在，进行一些准备工作：检查文件所在目录是否存在，如果不存在则创建
        File fileFolder = new File(fileFolderPath);
        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 得到文件所属目录的路径
     *
     * @param fileMD5 fileMD5
     * @return 路径
     */
    private String getFileFolderPath(String fileMD5) {
        return uploadLocation + fileMD5.charAt(0) + File.separator + fileMD5.charAt(1) + File.separator + fileMD5 + File.separator;
    }

    /**
     * 得到文件路径
     *
     * @param fileMD5       fileMD5
     * @param fileExtension fileExtension
     * @return String
     */
    private String getFilePath(String fileMD5, String fileExtension) {
        return getFileFolderPath(fileMD5) + fileMD5 + "." + fileExtension;
    }

    /**
     * 得到块文件所属目录
     *
     * @return String
     */
    private String getChunkFileFolderPath(String fileMD5) {
        return getFileFolderPath(fileMD5) + File.separator + "chunk" + File.separator;
    }


    /**
     * 检查分块文件是否存在
     *
     * @param fileMD5    fileMD5
     * @param chunkIndex chunkIndex
     * @param chunkSize  chunkSize
     * @return CheckChunkResult
     */
    public CheckChunkResult checkChunk(String fileMD5, Integer chunkIndex, Long chunkSize) {
        String chunkFileFolderPath = getChunkFileFolderPath(fileMD5);
        //块文件
        File chunkFile = new File(chunkFileFolderPath + chunkIndex);
        if (chunkFile.exists()) {
            //块文件存在
            return new CheckChunkResult(CommonCode.SUCCESS, true);
        } else {
            //块文件不存在
            return new CheckChunkResult(CommonCode.SUCCESS, false);
        }
    }

    public ResponseResult uploadChunk(String fileMD5, Integer chunkIndex, MultipartFile file) throws IOException {
        //检查分块文件的目录，如果不存在则创建
        String chunkFileFolderPath = getChunkFileFolderPath(fileMD5);
        String chunkFilePath = chunkFileFolderPath + chunkIndex;
        File chunkFileFolder = new File(chunkFileFolderPath);
        //分块文件目录不存在则创建
        if (!chunkFileFolder.exists()) {
            chunkFileFolder.mkdirs();
        }

        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            //得到上传文件的输入流
            inputStream = file.getInputStream();
            fileOutputStream = new FileOutputStream(chunkFilePath);
            IOUtils.copy(inputStream, fileOutputStream);
        } finally {
            assert inputStream != null;
            inputStream.close();
            assert fileOutputStream != null;
            fileOutputStream.close();
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 合并所有的chunk
     *
     * @param fileMD5       fileMD5
     * @param fileName      fileName
     * @param fileSize      fileSize
     * @param mimeType      mimeType
     * @param fileExtension fileExtension
     * @return ResponseResult
     */
    public ResponseResult mergeChunk(String fileMD5, String fileName, Long fileSize, String mimeType, String fileExtension) {

        //1.合并所有chunk
        //得到分块文件的所属目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMD5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        //分块文件列表
        File[] chunkFiles = chunkFileFolder.listFiles();

        //创建一个合并文件
        String filePath = this.getFilePath(fileMD5, fileExtension);
        File mergedFile = new File(filePath);

        //执行合并

        //2.校验合并文件后的MD5是否和前端传入的MD5值保持一致


        //3.将文件的信息写入MongoDB中


        return null;
    }
}
