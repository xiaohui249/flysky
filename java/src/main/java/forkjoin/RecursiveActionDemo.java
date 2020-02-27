package forkjoin;

import java.util.concurrent.RecursiveAction;

/**
 * @Description RecursiveAction示例类，该类将递归地将大任务拆分为小任务（仅输出不超过20的数字）
 * @author sean
 * @date 2020/2/27
 */
public class RecursiveActionDemo extends RecursiveAction {

    // 每个小任务最多打印20个数字
    private static final int MAX = 20;

    private int start;
    private int end;

    public RecursiveActionDemo(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        if((end - start) <= MAX ) {
            for(int i=start; i<end; i++) {
                System.out.println("线程" +Thread.currentThread().getName()+ "输出i的值为：" + i);
            }
        } else {
            //将大任务拆分为两个小任务
            int middle = (start + end) / 2;
            RecursiveActionDemo left = new RecursiveActionDemo(start, middle);
            RecursiveActionDemo right = new RecursiveActionDemo(middle, end);
            left.fork();
            right.fork();
        }
    }
}
