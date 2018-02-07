package com.sean.flysky.utils.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * the des utils
 * DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同密钥的算法。
 * 注意：DES加密和解密过程中，密钥长度都必须是8的倍数
 *
 * @author xiaoh
 * @create 2017-11-20 20:02
 **/
public class DESUtils {
    //密码，长度要是8的倍数
    public static final String PASSWORD_CRYPT_KEY = "12345678";
    public static final String DES_ALGORITHM = "DES";   // 定义加密算法

    public static String encrypt(String encryptString, String encryptKey)
            throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();

        DESKeySpec desKey = new DESKeySpec(encryptKey.getBytes());
        //创建一个密匙工厂，然后用它把DESKeySpec转换成
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
        SecretKey key = keyFactory.generateSecret(desKey);

        Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, random);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
        return Base64Utils.encode(encryptedData);
    }

    public static String decrypt(String decryptString, String decryptKey)
            throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(decryptKey.getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey key = keyFactory.generateSecret(desKey);

        Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, random);

        byte decryptedData[] = cipher.doFinal(Base64Utils.decode(decryptString));
        return new String(decryptedData);
    }

    public static void main(String[]args) throws Exception{
        String plainText = "hahaGG123哈哈";
        String cipherText = DESUtils.encrypt(plainText, DESUtils.PASSWORD_CRYPT_KEY);
        System.out.println("明文：" + plainText);
        System.out.println("密钥：" + DESUtils.PASSWORD_CRYPT_KEY);
        System.out.println("密文：" + cipherText);
        System.out.println("解密后：" + DESUtils.decrypt(cipherText, DESUtils.PASSWORD_CRYPT_KEY));
    }
}
