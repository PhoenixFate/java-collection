package com.phoenix.netty.one;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;

/**
 * 通过 FileInputStream 获取的 channel 只能读
 * 通过 FileOutputStream 获取的 channel 只能写
 * 通过 RandomAccessFile 是否能读写根据构造 RandomAccessFile 时的读写模式决定
 *
 * @Author phoenix
 * @Date 2022/10/28 17:26
 * @Version 1.0.0
 */
public class TestFileChannelTransferTo {

    public static void main(String[] args) {
        try (
                FileChannel from = new FileInputStream(new File(TestFileChannelTransferTo.class.getResource("/from.txt").toURI())).getChannel();
                FileChannel to = new FileOutputStream(new File(TestFileChannelTransferTo.class.getResource("/to.txt").toURI())).getChannel();
        ) {
            //效率高，底层会利用操作系统的零拷贝法进行优化
            from.transferTo(0, from.size(), to);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public static void main2(String[] args) {
        System.out.println("++++++++++++++++++++++++");
        String path = System.getProperty("java.class.path");
        String path2 = TestFileChannelTransferTo.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String path3 = TestFileChannelTransferTo.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        String path4 = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        URL url = TestFileChannelTransferTo.class.getResource("/");

        System.out.println(System.getProperty("java.home"));
        System.out.println("path 1 = " + path);
        System.out.println("path 2 = " + path2);
        System.out.println("path 3 = " + path3);
        System.out.println("path 4 = " + path4);
        assert url != null;
        System.out.println(url.getPath());
        System.out.println("++++++++++++++++++++++++");
    }

}
