package oom;

import java.util.ArrayList;
import java.util.List;

/**
 * 堆内存溢出
 *
 * @author xiaoh
 * @create 2019-02-22 12:59
 **/
public class HeapOOM {
    static class OOMObject{}

    public static void main(String[] args) throws Exception {
        List<OOMObject> list = new ArrayList<>();
        while(true) {
            System.out.println("size: " + list.size());
            Thread.sleep(1000);
            list.add(new OOMObject());
        }
    }
}
