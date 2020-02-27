package thread;

public class MyRunnable implements Runnable {
    private int ticket = 5;
    @Override
    public void run() {
        while(true){
            if (ticket > 0) {
                System.out.println("线程" + Thread.currentThread().getName() + "卖出车票第" + ticket-- + "张");
            } else {
                break;
            }
        }
    }
}
