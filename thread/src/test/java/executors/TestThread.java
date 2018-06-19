package executors;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-6-21
 * Time: 下午6:17
 * 线程池的基本思想还是一种对象池的思想，开辟一块内存空间，里面存放了众多(未死亡)的线程，池中线程执行调度由池管理器来处理。
 * 当有线程任务时，从池中取一个，执行完成后线程对象归池，这样可以避免反复创建线程对象所带来的性能开销，节省了系统的资源。
 * 线程任务与线程的关系，相当于work与worker。当线程任务大于线程池中线程数量时，线程池中的一个线程可能执行多个线程任务。
 */
public class TestThread {
    public static void main(String[] args) {
        int n = 4;

        //创建一个可重用固定线程数的线程池
//        ExecutorService pool = Executors.newFixedThreadPool(n);

        //创建一个使用单个 worker 线程的 Executor，以无界队列方式来运行该线程。
//        ExecutorService pool = Executors.newSingleThreadExecutor();

        //创建一个可根据需要创建新线程的线程池，但是在以前构造的线程可用时将重用它们。
//        ExecutorService pool = Executors.newCachedThreadPool();

        //创建一个线程池，它可安排在给定延迟后运行命令或者定期地执行
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);

        //创建一个线程池，它可安排在给定延迟后运行命令或者定期地执行
//        ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();

        pool.schedule(new LateThread(), 10, TimeUnit.MILLISECONDS);

        //创建等待队列
//        BlockingQueue<Runnable> bqueue = new ArrayBlockingQueue<Runnable>(20);
        //创建一个单线程执行程序，它可安排在给定延迟后运行命令或者定期地执行。
//        ThreadPoolExecutor pool = new ThreadPoolExecutor(2,3,2,TimeUnit.MILLISECONDS,bqueue);

        for(int i=0; i< (n+1); i++) {
            pool.execute(new MyThread());
        }

        pool.shutdown();
    }
}
