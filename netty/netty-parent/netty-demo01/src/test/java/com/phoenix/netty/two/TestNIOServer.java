package com.phoenix.netty.two;

import com.phoenix.netty.one.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * 非阻塞服务端
 *
 * @Author phoenix
 * @Date 10/28/22 23:41
 * @Version 1.0
 */
@Slf4j
public class TestNIOServer {

    public static void main(String[] args) throws IOException {
        //单线程模式
        //0.ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
        //1.创建服务器
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false); //设置为非阻塞模式
        //2.绑定监听端口
        serverSocketChannel.bind(new InetSocketAddress(8084));
        //3.建立连接的集合
        List<SocketChannel> socketChannelList = new ArrayList<>();
        while (true) {
            //4.accept 建立与客户端的连接，SocketChannel用来与客户端之间通信
            //log.debug("connection......");
            SocketChannel socketChannel = serverSocketChannel.accept();//已改成非阻塞，如果没有建立连接，但socketChannel为null
            if (socketChannel != null) {
                log.debug("connected.....{}", socketChannel);
                socketChannel.configureBlocking(false);//设置为非阻塞模式
                socketChannelList.add(socketChannel);
            }
            for (SocketChannel channel : socketChannelList) {
                //5.接收客户端发送的数据
                //log.debug("before read.... {}", channel);
                int read = channel.read(buffer);//也改成来非阻塞方法，线程仍然继续运行，如果没有读到数据，read返回0
                if (read > 0) {
                    buffer.flip();//buffer切换读模式
                    ByteBufferUtil.debugAll(buffer);
                    buffer.clear();//buffer切换写模式
                    log.debug("after read....{}", channel);
                }
            }
        }
    }

}
