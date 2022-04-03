package com.xuecheng.test.fastdfs.controller;

import com.xuecheng.test.fastdfs.config.FastDFSClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author D-L
 * @version 1.0.0
 * @ClassName FastDFSController.java
 * @Description fastDFS文件上传下载
 * @createTime 2021-07-07 10:55:00
 */
@Slf4j
@RestController
@RequestMapping("/fastDFS")
public class FastDFSController {
    @Autowired
    private FastDFSClient fastDFSClient;

    @PostMapping("/upload")
    public String uploadFile(MultipartFile file) throws IOException {
        System.setProperty("sun.net.client.defaultConnectTimeout", String
                .valueOf(10000));// （单位：毫秒）
        System.setProperty("sun.net.client.defaultReadTimeout", String
                .valueOf(10000)); // （单位：毫秒）

        
        String result ="";
        try {
            byte[] bytes = file.getBytes();
            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
            long fileSize = file.getSize();
            result = fastDFSClient.uploadFile(bytes, fileSize, extension);
        }catch (Exception e) {
            log.error("fastDFS 文件上传失败------fileName:{}" , file.getName());
        }
        return result;
    }

    /**
     *
     * @param fileUrl  group1/M00/00/00/wKgKGWDlIo-AKfngAAFlGYb7GuA61.xlsx
     * @param response
     */
    @GetMapping("/download")
    public void downloadFile(String fileUrl, HttpServletResponse response){
        String suffix = fileUrl.substring(fileUrl.lastIndexOf("."));
        byte[] bytes = new byte[0];
        try {
            bytes = fastDFSClient.downloadFile(fileUrl);
            response.reset();
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("fastDFS" + suffix, "UTF-8"));
            response.setCharacterEncoding("UTF-8");
        } catch (IOException e) {
            log.error("fastDFS 下载文件失败------fileUrl:{}" , fileUrl);
        }
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}