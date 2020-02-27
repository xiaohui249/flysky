import java.util.HashMap;
import java.util.Map;

public class SingleMapMain {

    final static String KEY = "key";

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>(1);

        Thread producerThread = new Producer(map);
        Thread consumerThread = new Consumer(map);

        producerThread.start();
        consumerThread.start();
    }

    static class Producer extends Thread {

        private Map<String, Integer> commonMap;

        public Producer(Map<String, Integer> map) {
            this.commonMap = map;
        }

        @Override
        public void run() {
            int i = 1;
            while(true) {
                synchronized (commonMap) {
                    if(commonMap.size() == 0 && i <= 100) {
                        commonMap.put(KEY, i);
                        System.out.println("put " + i);
                        i++;
                        commonMap.notify();
                    }

                    if(i > 100) {
                        break;
                    }
                }
            }
        }
    }

    static class Consumer extends Thread {

        private Map<String, Integer> commonMap;

        public Consumer(Map<String, Integer> map) {
            this.commonMap = map;
        }

        int i = 0;
        int sum = 0;
        @Override
        public void run() {
            while (true) {
                synchronized (commonMap) {
                    if(i == 100) {
                        break;
                    }
                    if(commonMap.size() == 0) {
                        try {
                            commonMap.wait();
                        }catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Integer num = commonMap.get(KEY);
                    sum += num;
                    System.out.println("本次取值为：" + num + ", 累加和为：" + sum);
                    commonMap.remove(KEY);
                    i++;
                }
            }
        }
    }
}
