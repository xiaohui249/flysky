package com.flysky.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 使用BufferReader读取文件
 *
 * @author xiaoh
 * @create 2018-11-17 15:13
 **/
public class ReadByBufferReader {

    public static void read(String file) {
        try(BufferedReader reader = new BufferedReader(new FileReader((file)))) {
            String content;
            long s = System.currentTimeMillis();
            while((content = reader.readLine()) != null) {
                System.out.println(content);
            }
            System.out.println("buffer reader cost: " + (System.currentTimeMillis() - s));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
