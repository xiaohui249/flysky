import forkjoin.RecursiveActionDemo;
import forkjoin.RecursiveTaskDemo;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * @author sean
 * @Description: ForkJoin框架测试类(用一句话描述该文件做什么)
 * @date 2020/2/27
 */
public class ForkJoinTest {
    public static void main(String[] args) throws Exception {
        //创建包含Runtime.getRuntime().availableProcessors()返回值作为个数的并行线程的ForkJoinPool
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        //提交可分解的PrintTask任务
//        forkJoinPool.submit(new RecursiveActionDemo(0, 1000));
        //阻塞当前线程直到 ForkJoinPool 中所有的任务都执行结束
//        forkJoinPool.awaitTermination(2, TimeUnit.SECONDS);

        //提交可分解的ComputeTask任务
        Integer integer = forkJoinPool.invoke( new RecursiveTaskDemo(1, 100));
        System.out.println("计算出来的总和=" + integer);

        // 关闭线程池
        forkJoinPool.shutdown();
    }
}

