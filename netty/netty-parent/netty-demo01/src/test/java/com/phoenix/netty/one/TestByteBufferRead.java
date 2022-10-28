package com.phoenix.netty.one;

import java.nio.ByteBuffer;

/**
 * ByteBuffer 读取数据
 *
 * @Author phoenix
 * @Date 2022/10/28 10:13
 * @Version 1.0.0
 */
public class TestByteBufferRead {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put(new byte[]{'a', 'b', 'c', 'd'});

        byteBuffer.flip();
        //从头开始读
        byteBuffer.get(new byte[4]);
        ByteBufferUtil.debugAll(byteBuffer);

        //数据反复的读：rewind
        byteBuffer.rewind();
        System.out.println((char) byteBuffer.get());

        //mark&reset  （rewind的增强）
        //mark做一个标记，记录position位置，reset是将position重置到mark的位置
        System.out.println((char) byteBuffer.get());
        byteBuffer.mark(); // 加标记，索引为2的位置
        System.out.println("-------------- 标记开始 -------------");
        System.out.println((char) byteBuffer.get());
        System.out.println((char) byteBuffer.get());
        System.out.println("-------------- 标记结束 -------------");
        byteBuffer.reset(); // 将position的位置重置到上次做标记的位置
        System.out.println("-------------- 从上次标记的位置开始 -------------");
        System.out.println((char) byteBuffer.get());
        System.out.println((char) byteBuffer.get());
        System.out.println("-------------- 结束 -------------");

        //byteBuffer.get(i) 获取position为i的内容，但position的位置不变
        System.out.println((char) byteBuffer.get(2));
        ByteBufferUtil.debugAll(byteBuffer);

    }

}
