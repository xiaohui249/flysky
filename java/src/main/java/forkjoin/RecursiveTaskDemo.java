package forkjoin;

import java.util.concurrent.RecursiveTask;

/**
 * @author sean
 * @Description: RecursiveTask任务示例类
 * @date 2020/2/27
 */
public class RecursiveTaskDemo extends RecursiveTask<Integer> {

    private final static int MAX = 10;
    private int start;
    private int end;

    public RecursiveTaskDemo(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        if((end - start) <= 10) {
            for(int i=start; i<=end; i++) {
               sum += i;
            }
        } else {
            System.out.println("任务分解！");
            int middle = (start + end) / 2;
            RecursiveTaskDemo left = new RecursiveTaskDemo(start, middle);
            RecursiveTaskDemo right = new RecursiveTaskDemo(middle+1, end);
            left.fork();
            right.fork();
            sum = left.join() + right.join();
        }
        return sum;
    }
}
