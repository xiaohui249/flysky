package com.flysky.file;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * 使用Guava一次性读取文件
 *
 * @author xiaoh
 * @create 2018-11-17 15:41
 **/
public class ReadAllByGuava {
    public static void read(String file) throws Exception {
        long s = System.currentTimeMillis();
        List<String> lines = Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8);
        System.out.println("guava read-all cost: " + (System.currentTimeMillis() - s) + ", lines: " + lines.size());
    }
}
