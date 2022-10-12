package com.phoenix.util;

import java.util.Base64;

/**
 * BASE64加密/解密
 * <p>
 * Base64 编码是我们程序开发中经常使用到的编码方法，它用 64 个可打印字符来表示二进制数据。这 64 个字符是：
 * 小写字母 a-z、大写字母 A-Z、数字 0-9、符号"+“、”/“（再加上作为垫字的”="，实际上是 65 个字符），
 * 其他所有符号都转换成这个字符集中的字符。Base64 编码通常用作存储、传输一些二进制数据编码方法，
 * 所以说它本质上是一种将二进制数据转成文本数据的方案。
 * <p>
 * 通常用作对二进制数据进行加密
 *
 * @Author phoenix
 * @Date 2022/10/12 14:43
 * @Version 1.0.0
 */
public class Base64Util {

    /***
     * BASE64解密
     * @param encryptedData 加密后的字符串
     * @return 解密后的字符串
     */
    public static String decryptBASE64(String encryptedData) {
        return new String(Base64.getDecoder().decode(encryptedData));
    }

    /***
     * BASE64加密
     * @param dataToEncrypt 需要加密的字符串
     * @return base64加密后的字符串
     */
    public static String encryptBASE64(String dataToEncrypt) {
        return Base64.getEncoder().encodeToString(dataToEncrypt.getBytes());
    }

    public static void main(String[] args) {
        String encryptedString = Base64Util.encryptBASE64("abc");
        System.out.println(encryptedString);
        String decryptedString = Base64Util.decryptBASE64(encryptedString);
        System.out.println(decryptedString);
    }
}