package com.leyou.upload.service.impl;

import com.leyou.myexception.ExceptionEnum;
import com.leyou.myexception.LeyouException;
import com.leyou.upload.config.UploadProperties;
import com.leyou.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@EnableConfigurationProperties(UploadProperties.class)
public class UploadServiceImpl implements UploadService {


    @Autowired
    private UploadProperties uploadProperties;

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            // 校验文件类型
            String contentType = file.getContentType();
            if(!uploadProperties.getAllowTypes().contains(contentType)){
                // 不允许的上传文件的类型
                throw new LeyouException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            // 校验文件的内容
            // 如果file里面不是图片，则BufferedImage为空或抛出异常
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image==null){
                throw new LeyouException(ExceptionEnum.INVALID_FILE_TYPE);
            }

            // 准备目标路径
            File destination=new File(this.getClass().getResource("/").getPath(),file.getOriginalFilename());
            // 保存文件到本地
            file.transferTo(destination);
            // 返回路径
            return uploadProperties.getBaseUrl()+file.getOriginalFilename();
        } catch (IOException e) {
            // 上传失败
            // 记录日志
            log.error("上传文件失败",e);
            throw new LeyouException(ExceptionEnum.UPLOAD_IMAGE_ERROR);
        }
    }

}
