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
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/6/6 15:12
 */
@Service
public class MediaUploadService {

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Value("${xc-service-manage-media.upload-location}")
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
        return getFileFolderPath(fileMD5) + "chunk" + File.separator;
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

    /**
     * 上传文块文件
     *
     * @param fileMD5
     * @param chunkIndex
     * @param file
     * @return
     * @throws IOException
     */
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
        //文块文件目录
        File chunkFileFolder = new File(chunkFileFolderPath);
        //分块文件列表
        File[] chunkFiles = chunkFileFolder.listFiles();
        if (chunkFiles == null || chunkFiles.length == 0) {
            //合并文件失败
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
        }

        //创建一个合并文件
        String filePath = this.getFilePath(fileMD5, fileExtension);
        File mergedFile = new File(filePath);

        //执行合并
        mergedFile = this.mergeFile(Arrays.asList(chunkFiles), mergedFile);
        if (mergedFile == null) {
            //合并文件失败
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
        }

        //2.校验合并文件后的MD5是否和前端传入的MD5值保持一致
        boolean checkFileMD5 = this.checkFileMD5(mergedFile, fileMD5);
        if (!checkFileMD5) {
            //校验文件失败
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }

        //3.将文件的信息写入MongoDB中
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMD5);
        mediaFile.setFileOriginalName(fileName);
        mediaFile.setFileName(fileMD5 + "." + fileExtension);
        //文件的相对路径
        mediaFile.setFilePath(fileMD5.charAt(0) + File.separator + fileMD5.charAt(1) + File.separator + fileMD5 + File.separator);
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimeType);
        mediaFile.setFileType(fileExtension);
        //状态为上传成功
        mediaFile.setFileStatus("301002");
        //调用保存到mongo
        mediaFileRepository.save(mediaFile);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //校验合并后的文件的md5
    private boolean checkFileMD5(File mergedFile, String MD5) {
        FileInputStream inputStream = null;
        try {
            //创建文件输入流
            inputStream = new FileInputStream(mergedFile);
            //得到文件的MD5
            String MD5Hex = DigestUtils.md5Hex(inputStream);

            //和传入的md5比较
            return MD5Hex.equalsIgnoreCase(MD5);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }


    //合并文件
    private File mergeFile(List<File> chunkFileList, File mergedFile) {
        try {
            if (mergedFile.exists()) {
                //合并后的文件存在，则删除
                mergedFile.delete();
            } else {
                //创建一个新文件
                mergedFile.createNewFile();
            }

            //对块文件进行排序
            Collections.sort(chunkFileList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                        return 1;
                    }
                    return -1;
                }
            });

            //创建一个写对象
            RandomAccessFile randomAccessFileWrite = new RandomAccessFile(mergedFile, "rw");
            byte[] b = new byte[1024];
            for (File chunkFile : chunkFileList) {
                RandomAccessFile randomAccessFileRead = new RandomAccessFile(chunkFile, "r");
                int len = -1;
                while ((len = randomAccessFileRead.read(b)) != -1) {
                    randomAccessFileWrite.write(b, 0, len);
                }
                randomAccessFileRead.close();
            }
            randomAccessFileWrite.close();
            return mergedFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
