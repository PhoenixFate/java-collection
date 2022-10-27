package com.phoenix.netty.one;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * ByteBuffer介绍
 *
 * @Author phoenix
 * @Date 10/19/22 23:42
 * @Version 1.0
 */
@Slf4j
public class TestByteBuffer {

    public static void main(String[] args) {
        URL resource = TestByteBuffer.class.getResource("/data.txt");
        if (resource == null) {
            throw new RuntimeException("resource is null");
        }
        // FileChannel
        // 1.输入输出流 2.RandomAccessFile
        try (FileInputStream fileInputStream = new FileInputStream(resource.getPath());
             FileChannel channel = fileInputStream.getChannel()) {
            //准备缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true) {
                //从channel读取数据，向buffer写入
                int length = channel.read(buffer);
                log.info("读取到的字节数: {}", length);
                if (length == -1) { //channel中没有内容了
                    break;
                }
                //打印buffer的内容
                buffer.flip();//buffer先切换至读模式
                while (buffer.hasRemaining()) {//是否还有剩余未读的数据
                    log.info("读取的单个字节: {}", (char) buffer.get());
                }
                //切换为写模式
                buffer.clear();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
