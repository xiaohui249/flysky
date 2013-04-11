package dbpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-10
 * Time: 上午12:01
 * To change this template use File | Settings | File Templates.
 */
public class TestDBPool {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i=0; i<15; i++) {
            exec.execute(new QueryThread("thread"+i));
        }
        exec.shutdown();
    }
}
