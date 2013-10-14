package com.sean;

import com.nsq.NSQProducer;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-10-14
 * Time: 下午4:36
 * To change this template use File | Settings | File Templates.
 */
public class NSQTest {
    public static void main(String[] args) throws Exception {
        NSQProducer producer = new NSQProducer().addAddress("10.10.76.98", 4150, 1);
        producer.start();
        String msg = "message";
        for (int i = 0; i < 50000; i++) {
            if (i % 1000 == 0) {
                System.out.println("producer: " + i);
            }
            producer.produceBatch("speedtest3", (msg + i).getBytes("utf8"));
        }

    }
}
