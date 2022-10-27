package com.phoenix.netty.one;

import java.nio.ByteBuffer;

/**
 * ByteBuffer读写模式切换以及一些事例
 *
 * @Author phoenix
 * @Date 10/28/22 01:04
 * @Version 1.0
 */
public class TestByteBufferReadWrite {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        //0x61为 16进制的91，而91就'a'
        buffer.put((byte) 0x61);
        ByteBufferUtil.debugAll(buffer);
        //写入'b' 'c' 'd'
        buffer.put(new byte[]{0x62, 0x63, 0x64});
        ByteBufferUtil.debugAll(buffer);
        // System.out.println(buffer.get());
        //切换成读模式，让position回到0
        buffer.flip();
        System.out.println(buffer.get());
        ByteBufferUtil.debugAll(buffer);
        //buffer.compact()把未读的内容往前移动，并且切换到写入模式
        buffer.compact();
        ByteBufferUtil.debugAll(buffer);
        buffer.put(new byte[]{0x65, 0x66});
        ByteBufferUtil.debugAll(buffer);

    }

}
