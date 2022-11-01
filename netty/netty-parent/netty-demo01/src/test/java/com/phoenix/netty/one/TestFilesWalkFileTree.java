package com.phoenix.netty.one;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * JDK1.7之前遍历文件树需要自己写递归，但在JDK1.7之后可以使用Files.walkFileTree来遍历文件树
 *
 * @Author phoenix
 * @Date 2022/10/28 18:07
 * @Version 1.0.0
 */
public class TestFilesWalkFileTree {

    public static void main(String[] args) throws IOException {
        //m2();

        m3();
    }

    private static void m3() throws IOException {
        //删除多级目录及目录下的文件
        Files.walkFileTree(Paths.get("test"), new SimpleFileVisitor<Path>() {
            /**
             * 删除文件
             */
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return super.visitFile(file, attrs);
            }

            /**
             * 退出目录时删除空目录
             */
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                System.out.println("<======== 退出" + dir);
                Files.delete(dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }

    /**
     * 所有以.jar结尾的文件
     *
     * @throws IOException IO异常
     */
    private static void m2() throws IOException {
        System.out.println(System.getProperty("java.home"));
        //m1();
        AtomicInteger fileCount = new AtomicInteger();
        Files.walkFileTree(Paths.get(System.getProperty("java.home")).getParent(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                //判断文件后缀是否以.jar结尾
                if (file.toString().endsWith(".jar")) {
                    System.out.println(file);
                    fileCount.incrementAndGet();
                }
                return super.visitFile(file, attrs);
            }
        });
        System.out.println("jar包数量: " + fileCount.get());
    }

    /**
     * 遍历某路径下的所有文件、文件夹
     *
     * @throws IOException IO异常
     */
    private static void m1() throws IOException {
        AtomicInteger dirCount = new AtomicInteger();
        AtomicInteger fileCount = new AtomicInteger();
        Files.walkFileTree(Paths.get(System.getProperty("java.home")), new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("=====>" + dir);
                dirCount.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file);
                fileCount.incrementAndGet();
                return super.visitFile(file, attrs);
            }
        });
        System.out.println(dirCount.get());
        System.out.println(fileCount.get());
    }

}
