package com.flysky.file;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * 使用Java8 Stream API读取文件
 *
 * @author xiaoh
 * @create 2018-11-17 15:13
 **/
public class ReadByStreamAPI {

    public static void read(String file) {
        try(Stream inputStream = Files.lines(Paths.get(file), StandardCharsets.UTF_8)) {
            long s = System.currentTimeMillis();
            inputStream.forEach(System.out::println);
            System.out.println("stream-api reader cost: " + (System.currentTimeMillis() - s));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
