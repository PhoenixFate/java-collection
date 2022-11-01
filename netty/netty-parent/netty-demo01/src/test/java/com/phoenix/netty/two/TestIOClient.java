package com.phoenix.netty.two;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * 客户端
 *
 * @Author phoenix
 * @Date 10/28/22 23:50
 * @Version 1.0
 */
public class TestIOClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8083));
        System.out.println("waiting.....");

        Thread.sleep(1000 * 10);
        socketChannel.write(Charset.defaultCharset().encode("hello!"));
        Thread.sleep(1000 * 10);
        socketChannel.write(Charset.defaultCharset().encode("hello!"));
        Thread.sleep(1000 * 10);

    }


}
