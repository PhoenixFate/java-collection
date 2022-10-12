package com.phoenix.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * AES（Advanced Encryption Standard） 加密/解密
 * <p>
 * 高级加密标准（英语：Advanced Encryption Standard，缩写：AES），在密码学中又称 Rijndael 加密法，
 * 是美国联邦政府采用的一种区块加密标准。这个标准用来替代原先的 DES，已经被多方分析且广为全世界所使用。简单说就是 DES 的增强版，比 DES 的加密强度更高。
 * <p>
 * AES 与 DES 一样，一共有四种加密模式：电子密码本模式（ECB）、加密分组链接模式（CBC）、加密反馈模式（CFB）和输出反馈模式（OFB）。
 * 关于加密模式的介绍，推荐这篇文章：高级加密标准AES的工作模式（ECB、CBC、CFB、OFB）
 *
 * @Author phoenix
 * @Date 2022/10/12 15:12
 * @Version 1.0.0
 */
public class AESUtil {

    public static final String algorithm = "AES";
    // AES/CBC/NOPadding
    // AES 默认模式
    // 使用CBC模式, 在初始化Cipher对象时, 需要增加参数, 初始化向量IV : IvParameterSpec iv = new
    // IvParameterSpec(key.getBytes());
    // NOPadding: 使用NOPadding模式时, 原文长度必须是8byte的整数倍
    // AES/CBC/PKCS5Padding 使用带填充的
    public static final String transformation = "AES/CBC/PKCS5Padding";
    public static final String secretKey = "phoenix012345678"; //Invalid AES key length
    public static final String ivKey = "1234567812345678"; //Wrong IV length: must be 16 bytes long

    /***
     * 加密
     * @param original 需要加密的参数（注意必须是16位）
     * @return 加密后的数据
     */
    public static String encryptByAES(String original) throws Exception {
        // 获取Cipher
        Cipher cipher = Cipher.getInstance(transformation);
        // 生成密钥
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), algorithm);
        // 指定模式(加密)和密钥
        // 创建初始化向量
        IvParameterSpec iv = new IvParameterSpec(ivKey.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        // cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        // 加密
        byte[] bytes = cipher.doFinal(original.getBytes());
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 解密
     * @param encrypted 需要解密的参数
     * @return 解密后的数据
     */
    public static String decryptByAES(String encrypted) throws Exception {
        // 获取Cipher
        Cipher cipher = Cipher.getInstance(transformation);
        // 生成密钥
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), algorithm);
        // 指定模式(解密)和密钥
        // 创建初始化向量
        IvParameterSpec iv = new IvParameterSpec(ivKey.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
        // 解密
        byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        return new String(bytes);
    }

    public static void main(String[] args) throws Exception {
        String encryptedData = AESUtil.encryptByAES("abc");
        System.out.println(encryptedData);
        System.out.println(AESUtil.decryptByAES(encryptedData));
    }


}