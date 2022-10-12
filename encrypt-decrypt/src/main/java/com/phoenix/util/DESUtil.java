package com.phoenix.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * DES(Data Encryption Standard)对称加密/解密
 * <p>
 * DES 是一种对称加密算法，所谓对称加密算法就是：加密和解密使用相同密钥的算法。DES 加密算法出自 IBM 的研究，后来被美国政府正式采用，之后开始广泛流传。
 * 但近些年使用越来越少，因为 DES 使用 56 位密钥，以现代的计算能力，24 小时内即可被破解。
 * 顺便说一下 3DES（Triple DES），它是 DES 向 AES 过度的加密算法，使用 3 条 56 位的密钥对数据进行三次加密。是 DES 的一个更安全的变形。
 * 它以 DES 为基本模块，通过组合分组方法设计出分组加密算法。比起最初的 DES，3DES 更为安全。
 * <p>
 * 使用 Java 实现 DES 加密解密，注意密码长度要是 8 的倍数。加密和解密的 Cipher 构造参数一定要相同，不然会报错。
 * <p>
 * 数据加密标准算法,和BASE64最明显的区别就是有一个工作密钥，该密钥既用于加密、也用于解密，并且要求密钥是一个长度至少大于8位的字符串
 *
 * @Author phoenix
 * @Date 2022/10/12 15:00
 * @Version 1.0.0
 */
public class DESUtil {

    private static final Key key;
    private static final String SEED = "phoenix";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String ALGORITHM = "DES";

    static {
        try {
            //生成DES算法对象
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
            //运用SHA1安全策略
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            //设置上密钥种子
            secureRandom.setSeed(SEED.getBytes());
            //初始化基于SHA1的算法对象
            generator.init(secureRandom);
            //生成密钥对象
            key = generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * 获取加密的信息
     * @param dataToEncrypt 待加密的数据
     * @return 加密后的数据
     */
    public static String encryptString(String dataToEncrypt) {
        //基于BASE64编码，接收byte[]并转换成String
        Base64.Encoder encoder = Base64.getEncoder();
        try {
            //按utf8编码
            byte[] bytes = dataToEncrypt.getBytes(CHARSET_NAME);
            //获取加密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            //初始化密码信息
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //加密
            byte[] doFinal = cipher.doFinal(bytes);
            //byte[]to encode好的String 并返回
            return new String(encoder.encode(doFinal));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /***
     * 获取解密之后的信息
     * @param encryptedData 加密后的数据
     * @return 解密后的数据
     */
    public static String decryptString(String encryptedData) {
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            //将字符串decode成byte[]
            byte[] bytes = decoder.decode(encryptedData);
            //获取解密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            //初始化解密信息
            cipher.init(Cipher.DECRYPT_MODE, key);
            //解密
            byte[] doFinal = cipher.doFinal(bytes);
            return new String(doFinal, CHARSET_NAME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String encryptedString = DESUtil.encryptString("abc");
        System.out.println(encryptedString);
        String originalData = DESUtil.decryptString(encryptedString);
        System.out.println(originalData);
    }
}
