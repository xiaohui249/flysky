package thread;

public class MyThread extends Thread {
    private int ticket = 5;
    private int ack = 0;

    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            if (ticket > 0) {
                System.out.println("线程" + Thread.currentThread().getName() + "卖出车票第" + ticket-- + "张");
            }
        }
    }
}
