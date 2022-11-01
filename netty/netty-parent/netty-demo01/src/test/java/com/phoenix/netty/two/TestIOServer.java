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
 * 使用NIO来理解阻塞
 *
 * @Author phoenix
 * @Date 10/28/22 23:41
 * @Version 1.0
 */
@Slf4j
public class TestIOServer {

    public static void main(String[] args) throws IOException {
        //单线程模式
        //0.ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
        //1.创建服务器
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2.绑定监听端口
        serverSocketChannel.bind(new InetSocketAddress(8083));
        //3.建立连接的集合
        List<SocketChannel> socketChannelList = new ArrayList<>();
        while (true) {
            //4.accept 建立与客户端的连接，SocketChannel用来与客户端之间通信
            log.debug("connection......");
            SocketChannel socketChannel = serverSocketChannel.accept();//默认是阻塞的，线程是暂停的
            log.debug("connected.....{}", socketChannel);
            socketChannelList.add(socketChannel);
            for (SocketChannel channel : socketChannelList) {
                //5.接收客户端发送的数据
                log.debug("before read.... {}", channel);
                channel.read(buffer);//也是阻塞方法，线程停止运行
                buffer.flip();//buffer切换读模式
                ByteBufferUtil.debugAll(buffer);
                buffer.clear();//buffer切换写模式
                log.debug("after read....{}", channel);
            }
        }
    }

}
