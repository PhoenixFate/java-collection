package com.phoenix.manage_media;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/6/2 16:11
 */
public class TestBlockFile {

    /**
     * 测试文件分块
     */
    @Test
    public void test01() throws IOException {
        //源文件
        File sourceFile = new File("E:\\work\\video\\lucene.avi");
        //块文件目录
        String chunksFileFolder = "E:\\work\\video\\chunks\\";
        //先定义块文件大小 1M
        long chunkFileSize = 1 * 1024 * 1024;
        //块数
        long chunkFileNumber = (long) Math.ceil(sourceFile.length() * 1.0 / chunkFileSize);

        //创建读文件的对象
        RandomAccessFile randomAccessFileRead = new RandomAccessFile(sourceFile, "r");
        //缓冲区
        byte[] b = new byte[1024];
        for (int i = 0; i < chunkFileNumber; i++) {
            File chunkFile = new File(chunksFileFolder + i);
            int len = -1;
            //创建向块文件写的对象
            RandomAccessFile randomAccessFileWrite = new RandomAccessFile(chunkFile, "rw");
            while ((len = randomAccessFileRead.read(b)) != -1) {
                //写
                randomAccessFileWrite.write(b, 0, len);
                //如果块文件的大小达到1M，则继续写一块文件
                if (chunkFile.length() >= chunkFileSize) {
                    break;
                }
            }
            randomAccessFileWrite.close();
        }
        randomAccessFileRead.close();
    }


    /**
     * 测试文件分块合并
     */
    @Test
    public void testMergeFile() throws IOException {
        //块文件目录
        String chunksFileFolderPath = "E:\\work\\video\\chunks\\";
        //块文件目录对象
        File chunkFileFolder = new File(chunksFileFolderPath);
        //块文件列表
        File[] files = chunkFileFolder.listFiles();
        //对文件名排序
        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                if (Integer.parseInt(f1.getName()) > Integer.parseInt(f2.getName())) {
                    return 1;
                }
                return -1;
            }
        });
        //合并文件
        File mergeFile = new File("E:\\work\\video\\lucene_merge.avi");
        //创建新文件
        boolean isSuccess = mergeFile.createNewFile();
        //创建写对象
        RandomAccessFile randomAccessFileWrite = new RandomAccessFile(mergeFile, "rw");
        byte[] b = new byte[1024];
        for (File chunkFile : fileList) {
            //创建读取块文件的对象
            RandomAccessFile randomAccessFileRead = new RandomAccessFile(chunkFile, "r");
            int len = -1;
            while ((len = randomAccessFileRead.read(b)) != -1) {
                randomAccessFileWrite.write(b, 0, len);
            }
            randomAccessFileRead.close();
        }
        randomAccessFileWrite.close();
    }


}
