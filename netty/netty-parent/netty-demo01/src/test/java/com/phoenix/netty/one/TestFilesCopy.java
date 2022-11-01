package com.phoenix.netty.one;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Author phoenix
 * @Date 10/28/22 23:03
 * @Version 1.0
 */
public class TestFilesCopy {

    public static void main(String[] args) throws IOException, URISyntaxException {
        String sourceTemp = TestFileChannelTransferTo.class.getResource("/").getPath();
        String source = sourceTemp.substring(0, sourceTemp.length() - 1);
        System.out.println(source);
        String target = Paths.get(TestFileChannelTransferTo.class.getResource("/").toURI())
                .getParent().toString() + File.separator + "test-classes-copy";
        System.out.println(target);

        Files.walk(Paths.get(source)).forEach(path -> {
            try {
                String targetName = path.toString().replace(source, target);
                System.out.println(targetName);
                if (Files.isDirectory(path)) {
                    //是目录，创建目录
                    Files.createDirectory(Paths.get(targetName));
                } else if (Files.isRegularFile(path)) {
                    //普通文件，则拷贝原始文件到目标文件
                    Files.copy(path, Paths.get(targetName));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

}
