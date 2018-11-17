package com.flysky.file;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 使用CommonIO一次性读取文件
 *
 * @author xiaoh
 * @create 2018-11-17 15:41
 **/
public class ReadAllByCommonIO {
    public static void read(String file) throws Exception {
        long s = System.currentTimeMillis();
        List<String> lines = FileUtils.readLines(new File(file), StandardCharsets.UTF_8.name());
        System.out.println("apache common-io read-all cost: " + (System.currentTimeMillis() - s) + ", lines: " + lines.size());
    }
}
