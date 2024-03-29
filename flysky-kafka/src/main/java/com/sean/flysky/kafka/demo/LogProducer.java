package com.sean.flysky.kafka.demo;

import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * Kafka生产者程序示例
 *
 * @author xiaoh
 * @create 2018-06-19 17:16
 **/
public class LogProducer {

    private final static Logger logger = LoggerFactory.getLogger(LogProducer.class);
    private final static String PRODUCER_CONFIG_FILE = "producer.properties";

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        try {
            props.load(LogProducer.class.getClassLoader().getResourceAsStream(PRODUCER_CONFIG_FILE));
        } catch (IOException e) {
            logger.error("Failed to load the config file of kafka producer!");
        }

        String topic = props.getProperty("topic", "");
        if(topic.trim().length() == 0) {
            logger.error("Please set topic!");
            System.exit(0);
        }
        alwaysProduce(props, topic);
    }

    /**
     * 定量生产数据，以便性能测试
     * @param props
     * @param topic
     */
    public static void notAlwaysProduce(Properties props, String topic) {
        Producer<String, String> producer = new KafkaProducer<>(props);
        long s = System.currentTimeMillis();
        int x = 0, y = 500000;
        for(int i=x; i<y; i++) {
            final String value = "message" + i;
            Future<RecordMetadata> future = producer.send(new ProducerRecord<String, String>(topic, value), new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if(null != exception) {
                        exception.printStackTrace();
                    } else {
                        logger.info(value + ", offset: " + metadata.offset());
                    }
                }
            });
//            RecordMetadata metadata = future.get();   // 同步生产
//            logger.info("log" + i + ", offset: " + metadata.offset());
        }
        logger.info("produce {} messages, cost {}ms", y-x, System.currentTimeMillis() - s);
        producer.close();
    }

    /**
     * 持续一直生产数据
     * @param props
     * @param topic
     */
    public static void alwaysProduce(Properties props, String topic) {
        Producer<String, String> producer = new KafkaProducer<>(props);
        int i = 1;
        while(true) {
            final String value = "message" + i;
            producer.send(new ProducerRecord<String, String>(topic, value), new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if(null != exception) {
                        exception.printStackTrace();
                    } else {
                        logger.info(value + ", offset: " + metadata.offset());
                    }
                }
            });
            i++;
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
