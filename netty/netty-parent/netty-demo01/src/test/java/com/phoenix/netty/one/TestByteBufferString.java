package com.phoenix.netty.one;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * ByteBuffer 与 String 之间的互相转换
 *
 * @Author phoenix
 * @Date 2022/10/28 10:46
 * @Version 1.0.0
 */
public class TestByteBufferString {

    public static void main(String[] args) {
        //1.字符串转为ByteBuffer
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(16);
        byteBuffer1.put("hello".getBytes());
        ByteBufferUtil.debugAll(byteBuffer1);

        //2.Charset
        ByteBuffer byteBuffer2 = StandardCharsets.UTF_8.encode("hello");
        ByteBufferUtil.debugAll(byteBuffer2);

        //3.wrap
        ByteBuffer byteBuffer3 = ByteBuffer.wrap("hello".getBytes());
        ByteBufferUtil.debugAll(byteBuffer3);

        //4.将ByteBuffer转换成字符串
        String string1 = StandardCharsets.UTF_8.decode(byteBuffer2).toString();
        System.out.println(string1);

        //如果该ByteBuffer没有切换到读模式，则无法转字符串
        //String string2 = StandardCharsets.UTF_8.decode(byteBuffer1).toString();
        //System.out.println(string2);

        byteBuffer1.flip();
        String string3 = StandardCharsets.UTF_8.decode(byteBuffer1).toString();
        System.out.println(string3);
    }


}
