package executors;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-6-21
 * Time: 下午6:19
 * To change this template use File | Settings | File Templates.
 */
public class MyThread extends Thread {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+" is running...");
    }
}
