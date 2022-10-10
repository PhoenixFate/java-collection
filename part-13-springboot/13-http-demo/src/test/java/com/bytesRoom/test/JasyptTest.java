package com.bytesRoom.test;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.junit.Test;

/**
 * Spring Boot项目application.yml文件数据库配置密码加密
 *
 * @Author phoenix
 * @Date 2022/10/10 11:05
 * @Version 1.0.0
 */
public class JasyptTest {

    @Test
    public void testEncrypt() throws Exception {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");          // 加密的算法，这个算法是默认的
        config.setPassword("phoenix");                       // 加密的密钥，必须为ASCll码
        standardPBEStringEncryptor.setConfig(config);
        String plainText = "abc";    //如：数据库密码
        String encryptedText = standardPBEStringEncryptor.encrypt(plainText);
        System.out.println(encryptedText);
    }

    @Test
    public void testDecrypt() throws Exception {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword("phoenix");
        standardPBEStringEncryptor.setConfig(config);
        String encryptedText = "PfDPA+y3YVDqtcZ0XvkPjL6AYJse5+da";    //执行上面方法得到的加密串
        String plainText = standardPBEStringEncryptor.decrypt(encryptedText);
        System.out.println(plainText);
    }
}
