package com.flysky.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 使用Apache common io读取文件
 *
 * @author xiaoh
 * @create 2018-11-17 15:13
 **/
public class ReadByApacheCommonIO {

    public static void read(String file) {
        try(LineIterator iterator = FileUtils.lineIterator(new File(file),StandardCharsets.UTF_8.name())) {
            long s = System.currentTimeMillis();
            while(iterator.hasNext()) {
                System.out.println(iterator.nextLine());
            }
            System.out.println("apache common-io reader cost: " + (System.currentTimeMillis() - s));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
