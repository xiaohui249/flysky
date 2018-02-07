package com.sean.flysky.utils.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * the md5 utils
 * MD5是一种信息摘要算法，主要是通过特定的hash散列方法将文本信息转换成简短的信息摘要，
 * 压缩+加密+hash算法的结合体，是绝对不可逆的。
 *
 * @author xiaoh
 * @create 2017-11-20 20:01
 **/
public class MD5Utils {
    /**
     * 全局数组
     */
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    /**
     * 转换字节为16进制字串
     * @param bByte
     * @return
     */
    private static String byteToHexString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    /**
     * 转换字节为16进制字串
     * @param bByte
     * @return
     */
    private static String byteToHexString2(byte bByte) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(strDigits[(bByte >>> 4) & 0x0f]);
        buffer.append(strDigits[bByte & 0x0f]);
        return buffer.toString();
    }

    /**
     * 转换字节为16进制字串
     * @param bByte
     * @return
     */
    private static String byteToHexString3(byte bByte) {
        String result = Integer.toHexString(bByte & 0xFF);
        if (result.length() == 1) {
            result = '0' + result;
        }
        return result;
    }

    /**
     *  转换字节数组为16进制字串
     * @param byteArray
     * @return
     */
    private static String byteArrayToHexString(byte[] byteArray) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            sBuffer.append(byteToHexString3(byteArray[i]));
        }
        return sBuffer.toString();
    }

    /**
     * 将给定的字符串经过md5加密后返回
     * @param str
     * @return
     */
    public static String encrypt(String str) {
        String resultString = null;
        try {
            //将给定字符串追加一个静态字符串，以提高复杂度
            resultString = new String(str);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    public static void main(String[]args){
        String time = "haha123456";
        System.out.println("明文："+time);
        String md5 = encrypt(time);
        System.out.println("密文："+md5);
    }
}
