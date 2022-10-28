package com.phoenix.netty.one;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 将文本中的多段内容一起通过FileChannel读取到不同的ByteBuffer中
 * @Author phoenix
 * @Date 2022/10/28 12:11
 * @Version 1.0.0
 */
public class TestScatteringReads {

    public static void main(String[] args) throws URISyntaxException {
        //将文本中的多段内容一起通过FileChannel读取到不同的ByteBuffer中
        URL url = TestScatteringReads.class.getResource("/words.txt");
        assert url != null;
        File file = new File(url.toURI());
        try (FileChannel fileChannel = new RandomAccessFile(file, "r").getChannel()) {
            ByteBuffer byteBuffer1 = ByteBuffer.allocate(3);
            ByteBuffer byteBuffer2 = ByteBuffer.allocate(3);
            ByteBuffer byteBuffer3 = ByteBuffer.allocate(5);
            fileChannel.read(new ByteBuffer[]{byteBuffer1, byteBuffer2, byteBuffer3});
            byteBuffer1.flip();
            byteBuffer2.flip();
            byteBuffer3.flip();
            ByteBufferUtil.debugAll(byteBuffer1);
            ByteBufferUtil.debugAll(byteBuffer2);
            ByteBufferUtil.debugAll(byteBuffer3);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
