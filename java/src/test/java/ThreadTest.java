import thread.MyCallable;
import thread.MyRunnable;
import thread.MyRunnable1;
import thread.MyThread;

import java.util.concurrent.FutureTask;

public class ThreadTest {
    public static void main(String[] args) {
        // MyThread类型线程测试
//        new MyThread("A").start();
//        new MyThread("B").start();
//        new MyThread("C").start();

        // MyRunnable类型线程测试
//        MyRunnable1 myRunnable = new MyRunnable1();
//        new Thread(myRunnable, "A").start();
//        new Thread(myRunnable, "B").start();
//        new Thread(myRunnable, "C").start();

        // MyCallable类型线程测试
        MyCallable myCallable = new MyCallable();
        FutureTask<Integer> futureTask = new FutureTask<>(myCallable);
        new Thread(futureTask, "线程名：有返回值的线程D").start();
        try {
            System.out.println("子线程的返回值：" + futureTask.get());
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("线程是否执行完毕：" + futureTask.isDone());
    }
}
