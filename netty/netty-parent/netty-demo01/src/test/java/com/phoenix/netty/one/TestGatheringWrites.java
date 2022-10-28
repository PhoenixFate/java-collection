package com.phoenix.netty.one;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * 讲多个ByteBuffer集中写入
 *
 * @Author phoenix
 * @Date 2022/10/28 12:25
 * @Version 1.0.0
 */
public class TestGatheringWrites {

    public static void main(String[] args) throws IOException, URISyntaxException {
        ByteBuffer byteBuffer1 = StandardCharsets.UTF_8.encode("hello");
        ByteBuffer byteBuffer2 = StandardCharsets.UTF_8.encode(" world");
        ByteBuffer byteBuffer3 = StandardCharsets.UTF_8.encode(" 你好");
        //将多个ByteBuffer组合到一起，一起写入文件
        URL url = TestGatheringWrites.class.getResource("/words2.txt");
        assert url != null;
        File file = new File(url.toURI());
        try (FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel()) {
            fileChannel.write(new ByteBuffer[]{byteBuffer1, byteBuffer2, byteBuffer3});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
