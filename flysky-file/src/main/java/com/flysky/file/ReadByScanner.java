package com.flysky.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 使用Scanner读取文件
 *
 * @author xiaoh
 * @create 2018-11-17 15:13
 **/
public class ReadByScanner {

    public static void read(String file) {
        try(Scanner fileScanner = new Scanner(new FileInputStream(file), StandardCharsets.UTF_8.name())) {
            long s = System.currentTimeMillis();
            while(fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
            System.out.println("scanner reader cost: " + (System.currentTimeMillis() - s));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
