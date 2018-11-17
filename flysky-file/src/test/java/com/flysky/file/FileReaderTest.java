package com.flysky.file;

/**
 * 文件读取测试
 *
 * @author xiaoh
 * @create 2018-11-17 15:32
 **/
public class FileReaderTest {
    public static void main(String[] args) throws Exception{
        String file = "E:/all.txt";

        // 逐行读取测试
//        ReadByBufferReader.read(file);
//        ReadByScanner.read(file);
//        ReadByStreamAPI.read(file);
//        ReadByApacheCommonIO.read(file);

        // 全部读取测试
        ReadAllByCommonIO.read(file);
        ReadAllByGuava.read(file);
    }
}
