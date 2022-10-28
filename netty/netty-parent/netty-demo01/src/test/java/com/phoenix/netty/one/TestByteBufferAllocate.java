package com.phoenix.netty.one;

import java.nio.ByteBuffer;

/**
 * @Author phoenix
 * @Date 2022/10/28 9:50
 * @Version 1.0.0
 */
public class TestByteBufferAllocate {

    public static void main(String[] args) {
        //分配空间的方法
        //当前java.nio.ByteBuffer的allocate分配内存不能动态变动
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        System.out.println(ByteBuffer.allocate(16).getClass()); //java.nio.HeapByteBuffer
        System.out.println(ByteBuffer.allocateDirect(16).getClass());//java.nio.DirectByteBuffer
        /*
         * java.nio.HeapByteBuffer Java堆内存，读写效率低，堆内存会受到gc（垃圾回收）的影响
         * java.nio.DirectByteBuffer 直接内存，读写效率高（少一次数据的拷贝），使用的系统内存，不会受到gc的影响，但分配内存的效率低
         */

    }

}
