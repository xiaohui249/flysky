package com.sean.flysky.utils.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * the rsa utils
 *
 * @author xiaoh
 * @create 2017-11-20 20:02
 **/
public class RSAUtils {
    private static final String DEFAULT_PUBLIC_KEY =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCu2ro7nroyw+09XLJlPGSHSdBkR6HRzyqfSqW3"+
                    "5bFfOZq1Jfk2QluoxxPjg9OfOabE/VZqS8HRoF/9m4Up4S+Mo28yshU0yd7vuDtEubJ16TY/fGYt"+
                    "gBWCM1qRw8YBVrAn0RBB5qQ/IZzv52y6h5Mq7jXc0w52svk2ZhZJTKUZzwIDAQAB";

    private static final String DEFAULT_PRIVATE_KEY =
            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK7aujueujLD7T1csmU8ZIdJ0GRH"+
                    "odHPKp9KpbflsV85mrUl+TZCW6jHE+OD0585psT9VmpLwdGgX/2bhSnhL4yjbzKyFTTJ3u+4O0S5"+
                    "snXpNj98Zi2AFYIzWpHDxgFWsCfREEHmpD8hnO/nbLqHkyruNdzTDnay+TZmFklMpRnPAgMBAAEC"+
                    "gYBaONJPYR+CgkYvm10Rj4/vns7Ab5W3xMWhW8sWkJZtJDQHtIhazvKsq8w/YQ4Y4o6CppWoU162"+
                    "46GkIlMbwO3fMAxKtTwl68Sf5MgUDI342XP5mWjuzcGL2TOa2L1MRJZ3Rv81/STMm7uZn7mtlCUH"+
                    "iEZvd/61SYXpH0Y+2yyTQQJBAOrrNmoovsHzoj/faQ4ix6yC5x/K71QbXIXfV7u1S6ftpaRbHcGy"+
                    "s7S5g6ixKBkiYwVptOX4+gnumnh5QgLB2g0CQQC+i6f3M6jIjQnuhH1WovWVsRTLDeMYSp+R/WgW"+
                    "S+cG+r4HtwqdQt4XAiX3fE3UeDPK5Qyfwju8J7hLAaNJzRhLAkAkU1bIt6A3sYrHQa6nBu3FWUCQ"+
                    "A/taEBkQgma4meInB3JzpbtGNnHfYkhfj3VZ3Z1eOuhEH2nam1C7jFSOxJcJAkBXFXyufe2lus/P"+
                    "Mr4V2mRjBYt7Qd2IPJKxJh9sEUSE7BPtcrpnJShbW5/+e9EWSdlh4UzjtuGWHJEdtqHqGNIBAkEA"+
                    "iUXjC7DHnEAdetaqZQwEOvZT8EZiMvRCXZYxdRqhf8C/hVQuqHM3VO/dvEWR3xrKU68J18WqOvtY"+
                    "yrARcHMPIw==";

    /**
     * 私钥
     */
    private static RSAPrivateKey privateKey;

    /**
     * 公钥
     */
    private static RSAPublicKey publicKey;

    static{
        //加载公钥
        try {
            //解决BC问题
            Security.addProvider(new BouncyCastleProvider());
            loadPublicKey(RSAUtils.DEFAULT_PUBLIC_KEY);
            System.out.println("加载公钥成功");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("加载公钥失败");
        }
        //加载私钥
        try {
            loadPrivateKey(RSAUtils.DEFAULT_PRIVATE_KEY);
            System.out.println("加载私钥成功");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("加载私钥失败");
        }
    }
    /**
     * 字节数据转字符串专用集合
     */
    private static final char[] HEX_CHAR= {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * 获取私钥
     * @return 当前的私钥对象
     */
    private static RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * 获取公钥
     * @return 当前的公钥对象
     */
    private static RSAPublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * 随机生成密钥对
     */
    private static void genKeyPair(){
        KeyPairGenerator keyPairGen= null;
        try {
            keyPairGen= KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair= keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        byte[] privateBytes = privateKey.getEncoded();
        String privateStr = Base64.encodeBase64String(privateBytes);
        byte[] publicBytes= publicKey.getEncoded();
        String publicStr = Base64.encodeBase64String(publicBytes);
        System.out.println("privateKey:"+privateStr);
        System.out.println("publicKey:"+publicStr);
    }

    /**
     * 从文件中输入流中加载公钥
     * @param in 公钥输入流
     * @throws Exception 加载公钥时产生的异常
     */
    @SuppressWarnings("unused")
    private  static void  loadPublicKey(InputStream in) throws Exception{
        try {
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            String readLine;
            StringBuilder sb= new StringBuilder();
            while((readLine= br.readLine())!=null){
                if(readLine.charAt(0)=='-'){
                    continue;
                }else{
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPublicKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }


    /**
     * 从字符串中加载公钥
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    private static void loadPublicKey(String publicKeyStr) throws Exception{
        try {
            byte[] buffer= Base64.decodeBase64(publicKeyStr);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
            publicKey= (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从文件中加载私钥
     * @param in 私钥文件名
     * @return 是否成功
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private  static void loadPrivateKey(InputStream in) throws Exception{
        try {
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            String readLine;
            StringBuilder sb= new StringBuilder();
            while((readLine= br.readLine())!=null){
                if(readLine.charAt(0)=='-'){
                    continue;
                }else{
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPrivateKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }
    /**
     * 从字符串中加载私钥
     * @param privateKeyStr
     * @throws Exception
     */
    private static void loadPrivateKey(String privateKeyStr) throws Exception{
        try {
            byte[] buffer= Base64.decodeBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            privateKey= (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    private static Cipher getPublicEncryptCipher(RSAPublicKey publicKey) throws Exception {
        if(publicKey== null){
            throw new Exception("加密公钥为空, 请设置");
        }

        Cipher cipher = Cipher.getInstance("RSA","BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher;
    }

    private static Cipher getPrivateEncryptCipher(RSAPrivateKey privateKey) throws Exception {
        if(privateKey== null){
            throw new Exception("加密私钥为空, 请设置");
        }
        Cipher cipher = Cipher.getInstance("RSA","BC");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher;
    }

    private static byte[] doEncrypt(Cipher cipher, byte[] plainTextData) throws Exception {
        try {
            byte[] output= cipher.doFinal(plainTextData);
            return output;
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * 公钥加密过程
     * @param publicKey 公钥
     * @param plainTextData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    private static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception{
        Cipher cipher = getPublicEncryptCipher(publicKey);
        return doEncrypt(cipher, plainTextData);
    }

    /**
     * 私钥加密过程
     * @param privateKey 公钥
     * @param plainTextData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    private static byte[] encryptByPrivate(RSAPrivateKey privateKey, byte[] plainTextData) throws Exception{
        Cipher cipher = getPrivateEncryptCipher(privateKey);
        return doEncrypt(cipher, plainTextData);
    }

    private static Cipher getPrivateDecryptCipher(RSAPrivateKey privateKey) throws Exception {
        if (privateKey == null){
            throw new Exception("解密私钥为空, 请设置");
        }
        Cipher cipher = Cipher.getInstance("RSA","BC");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher;
    }

    private static Cipher getPublicDecryptCipher(RSAPublicKey publicKey) throws Exception {
        if (publicKey == null){
            throw new Exception("解密公钥为空, 请设置");
        }
        Cipher cipher = Cipher.getInstance("RSA","BC");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher;
    }

    private static byte[] doDecrypt(Cipher cipher, byte[] cipherData) throws Exception {
        try {
            byte[] output= cipher.doFinal(cipherData);
            return output;
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }

    /**
     * 私钥解密过程
     * @param privateKey 私钥
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    private static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception{
        Cipher cipher = getPrivateDecryptCipher(privateKey);
        return doDecrypt(cipher, cipherData);
    }
    /**
     * 公钥解密过程
     * @param publicKey 私钥
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    private static byte[] decryptByPublicKey(RSAPublicKey publicKey, byte[] cipherData) throws Exception{
        Cipher cipher = getPublicDecryptCipher(publicKey);
        return doDecrypt(cipher, cipherData);
    }

    /**
     * decrypt String by Ras(sub) by privateKey
     * @return success:return decode original
     */
    public static String decryptByPrivateKey(String ciphertext) throws Exception{
        try {
            byte[] cipher =  Base64.decodeBase64(ciphertext);
            byte[] plainText = decrypt(getPrivateKey(), cipher);
            return new String(plainText,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * decrypt String by Ras(sub) by publicKey
     * @return success:return decode original
     */
    public static String decryptByPublicKey(String cipherText) throws Exception{
        try {
            byte[] cipher =  Base64.decodeBase64(cipherText);
            byte[] plainText = decryptByPublicKey(getPublicKey(), cipher);
            return new String(plainText,"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /**
     * encrypt String by Ras(add)  by publicKey
     * @return success:return encrypt Base64 String  ;  fail:"fail"
     * @throws Exception
     */
    public static String encryptByPublicKey(String plaintext) throws Exception{
        try {
            byte[] cipher = encrypt(getPublicKey(), plaintext.getBytes("utf-8"));
            return Base64.encodeBase64String(cipher);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * encrypt String by Ras(add)  by PrivateKey
     * @return success:return encrypt Base64 String
     * @throws Exception
     */
    public static String encryptByPrivateKey(String plaintext) throws Exception{
        try {
            byte[] cipher = encryptByPrivate(getPrivateKey(), plaintext.getBytes("utf-8"));
            return Base64.encodeBase64String(cipher);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args){
        //测试字符串
        String encryptStr= "12345678";
        try {
            System.out.println("=============私钥加密，公钥解密！===============");
            //私钥加密
            System.out.println("明文："+encryptStr);
            long encryptstart = System.currentTimeMillis();
            String cipherStr = encryptByPrivateKey(encryptStr);
            System.out.println("私钥加密密文："+cipherStr);
            long encryptend = System.currentTimeMillis();
            System.out.println("encrypt use time:"+(encryptend-encryptstart)+"ms");
            //公钥钥解密
            long decryptstart = System.currentTimeMillis();
            String plaineText = decryptByPublicKey(cipherStr);
            System.out.println("解密："+plaineText);
            long decryptend = System.currentTimeMillis();
            System.out.println("decrypt use time:"+(decryptend-decryptstart)+"ms");
            System.out.println("=============公钥加密，私钥解密！===============");
            //公钥加密
            System.out.println("明文："+encryptStr);
            encryptstart = System.currentTimeMillis();
            cipherStr = encryptByPublicKey(encryptStr);
            System.out.println("公钥加密密文："+cipherStr);
            encryptend = System.currentTimeMillis();
            System.out.println("encrypt use time:"+(encryptend-encryptstart)+"ms");
            //私钥解密
            decryptstart = System.currentTimeMillis();
            plaineText = decryptByPrivateKey(cipherStr);
            System.out.println("解密："+plaineText);
            decryptend = System.currentTimeMillis();
            System.out.println("decrypt use time:"+(decryptend-decryptstart)+"ms");
            System.out.println("===========获取私钥公钥============");
            genKeyPair();//获取密钥对
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
