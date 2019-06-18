package oom;

/**
 * GCDemo
 *
 * @author xiaoh
 * @date 2019/6/18
 */
public class GCDemo {
    public static void main(String[] args) {
        // allocate 4M space
        byte[] b = new byte[4 * 1024 * 1024];
        System.out.println("first allocate");
        // allocate 4M space
        b = new byte[4 * 1024 * 1024];
        System.out.println("second allocate");
    }
}
