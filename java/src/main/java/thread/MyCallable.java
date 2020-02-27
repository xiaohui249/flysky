package thread;

import java.util.concurrent.Callable;

public class MyCallable implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("当前线程名——" + Thread.currentThread().getName());
        int i = 0;
        for (; i < 5; i++) {
            System.out.println("循环变量i的值：" + i);
        }
        return i;
    }
}
