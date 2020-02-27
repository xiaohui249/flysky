package thread;

import java.util.concurrent.atomic.AtomicInteger;

public class MyRunnable1 implements Runnable {
    private volatile AtomicInteger ticket = new AtomicInteger(5);
    @Override
    public void run() {
        while(true){
            if (ticket.get() > 0) {
                System.out.println("线程" + Thread.currentThread().getName() + "卖出车票第" + ticket.getAndDecrement() + "张");
            } else {
                break;
            }
        }
    }
}
