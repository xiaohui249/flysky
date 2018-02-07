package com.sean.flysky.utils.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;

/**
 * 3DES加密解密的工具类
 *
 * @author xiaoh
 * @create 2017-11-21 12:54
 **/
public class DES3Utils {
    //  定义加密算法，有DES、DESede(即3DES)、Blowfish
    private static final String Algorithm = "DESede";
    //  加密
    private static final String PASSWORD_CRYPT_KEY = "2012PinganVitality075522628888ForShenZhenBelter075561869839";
    private static final Charset UFT8 = Charset.forName("UTF-8");

    /**
     * 3DES加密方法
     *
     * @param bytes 源数据的字节数组
     * @return
     */
    public static byte[] encrypt(byte[] bytes) {
        try {
            SecretKey desKey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);    //生成密钥
            Cipher c1 = Cipher.getInstance(Algorithm);    //实例化负责加密/解密的Cipher工具类
            c1.init(Cipher.ENCRYPT_MODE, desKey);    //初始化为加密模式
            return c1.doFinal(bytes);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 3DES加密方法，并将加密结果字节数组转为Base64字符串
     * @param bytes
     * @return
     */
    public static String encryptToString(byte[] bytes) {
        byte[] result = encrypt(bytes);
        return result == null ? null : DatatypeConverter.printBase64Binary(result);
    }

    /**
     * 先将待加密字符串转为byte数组，然后进行3DES加密方法，最后将加密结果字节数组转为Base64字符串
     * @param str
     * @return
     */
    public static String encryptToString(String str) {
        return str == null ? null : encryptToString(str.getBytes(UFT8));
    }

    /**
     * 3DES解密函数
     *
     * @param bytes 密文的字节数组
     * @return
     */
    public static byte[] decrypt(byte[] bytes) {
        try {
            SecretKey desKey = new SecretKeySpec(build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, desKey);    //初始化为解密模式
            return c1.doFinal(bytes);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 先将字符串进行Base64解码，然后进行3DES解码方法
     * @param str
     * @return
     */
    public static byte[] decrypt(String str) {
        return str == null ? null : decrypt(DatatypeConverter.parseBase64Binary(str));
    }

    /**
     * 先将字符串进行Base64解码，然后进行3DES解码方法，最后将解码得到的字节码组成字符串
     * @param str
     * @return
     */
    public static String decryptToString(String str) {
        byte[] result = decrypt(str);
        return result == null ? null : new String(result, UFT8);
    }

    private static byte[] build3DesKey(String keyStr) {
        byte[] key = new byte[24];    //声明一个24位的字节数组，默认里面都是0
        byte[] temp = keyStr.getBytes(UFT8);    //将字符串转成字节数组
        if(key.length > temp.length){
            //如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            //如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }

    public static void main(String[] args) {
        String msg = "3DES加密解密案例";
        System.out.println("【加密前】：" + msg);

        System.out.println("================================");

        //加密
        byte[] secretArr = DES3Utils.encrypt(msg.getBytes());
        System.out.println("【加密后】：" + new String(secretArr));

        //解密
        byte[] myMsgArr = DES3Utils.decrypt(secretArr);
        System.out.println("【解密后】：" + new String(myMsgArr));

        System.out.println("================================");

        //加密
        String encodeStr = DES3Utils.encryptToString(msg);
        System.out.println("【加密后】：" + encodeStr);

        //解密
        System.out.println("【解密后】：" + decryptToString(encodeStr));
    }
}
