package com.phoenix.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5(Message Digest Algorithm)加密
 * <p>
 * MD5 是将任意长度的数据字符串转化成短小的固定长度的值的单向操作，任意两个字符串不应有相同的散列值。因此 MD5 经常用于校验字符串或者文件，
 * 因为如果文件的 MD5 不一样，说明文件内容也是不一样的，如果发现下载的文件和给定的 MD5 值不一样，就要慎重使用。
 * <p>
 * MD5 主要用做数据一致性验证、数字签名和安全访问认证，而不是用作加密。比如说用户在某个网站注册账户时，输入的密码一般经过 MD5 编码，更安全的做法还会加一层盐（salt），
 * 这样密码就具有不可逆性。然后把编码后的密码存入数据库，下次登录的时候把密码 MD5 编码，然后和数据库中的作对比，这样就提升了用户账户的安全性。
 * <p>
 * 是一种单向加密算法，只能加密不能解密
 *
 * @Author phoenix
 * @Date 2022/10/12 14:50
 * @Version 1.0.0
 */
public class MD5Util {

    public static final String KEY_MD5 = "MD5";

    /***
     * MD5加密（生成唯一的MD5值）
     * @param data 需要加密的字符串
     * @return 加密后的字符串
     * @throws NoSuchAlgorithmException 算法不对异常
     */
    public static String encryptMD5(String data) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data.getBytes());
        StringBuilder result = new StringBuilder();
        byte[] bytes = md5.digest();
        //对byte数组进行遍历操作, 要不然直接把byte数组转字符串是乱码的
        for (byte aByte : bytes) {
            String tmp = Integer.toHexString(aByte & 0xFF);
            if (tmp.length() == 1) {
                result.append("0").append(tmp);
            } else {
                result.append(tmp);
            }
        }
        return result.toString();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println(MD5Util.encryptMD5("abc"));
    }

}
