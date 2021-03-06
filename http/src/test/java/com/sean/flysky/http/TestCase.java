package com.sean.flysky.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-8-29
 * Time: 上午9:55
 * 多线程测试驱动
 */
public class TestCase {

    private int sum = 1;
    private int concurrent = 1;

    private ExecutorService executorService;

    public TestCase() {
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    public TestCase(int sum, int concurrent) {
        if(concurrent > sum) concurrent = sum;
        this.sum = sum;
        this.concurrent = concurrent;
        executorService = Executors.newFixedThreadPool(concurrent);
    }

    public void test(Runnable thread) {
        for(int i = 0; i < sum; i++) {
            executorService.execute(thread);
        }
        executorService.shutdown();
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(int concurrent) {
        this.concurrent = concurrent;
    }

    public static void main(String[] args) {

        int total = 10000;
        int current = 1000;
        TestCase ts = new TestCase(total, current);
        ts.test(new HttpClientThread("http://localhost:8080/ibatis3Spring/index.do?uid=1"));
    }
}
