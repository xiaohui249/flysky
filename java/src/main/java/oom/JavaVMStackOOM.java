package oom;

/**
 * 创建线程导致内存溢出异常
 *
 * @author xiaoh
 * @create 2019-02-22 16:11
 **/
public class JavaVMStackOOM {
    private void doNotStop() {
        while (true) {
        }
    }

    public void stackLeakByThread() {
        while (true) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    doNotStop();
                }
            });
            thread.start();
        }
    }

    public static void main(String[] args) throws Throwable {
        JavaVMStackOOM oom = new JavaVMStackOOM();
        oom.stackLeakByThread();
    }
}
