package com.sean;

import com.nsq.*;
import com.nsq.lookup.NSQLookupDynMapImpl;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-10-14
 * Time: 下午4:36
 * To change this template use File | Settings | File Templates.
 */
public class NSQTest {

    static int i = 0;

    public static void main(String[] args) throws Exception {
//        producerStart();
        consumerStart();
    }

    public static void producerStart() {
        NSQProducer producer = new NSQProducer().addAddress("10.10.76.98", 4150, 1);
        producer.start();
        String msg = "message";
        for (int i = 0; i < 10000; i++) {
            if (i % 1000 == 0) {
                System.out.println("producer: " + i);
            }
            try{
                producer.produce("pub_term", (i + ":" + i).getBytes("utf8"));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void consumerStart() {

        NSQLookup lookup = new NSQLookupDynMapImpl();
        lookup.addAddr("10.10.76.98", 4161);

        NSQConsumer consumer = new NSQConsumer(lookup, "pub_term", "channel1", new NSQMessageCallback() {
            @Override
            public void message(NSQMessage nsqMessage) {
                try {
                    nsqMessage.finished();
                    String termInfo = new String(nsqMessage.getMessage(), "utf8");
                    System.out.println((i++) + "---" + termInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(Exception e) {
                System.err.println("nsq error" + e.getMessage());
            }
        });
        consumer.start();
    }


}
