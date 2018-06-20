package com.sean.flysky.kafka.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * LogConsumer
 *
 * @author xiaoh
 * @create 2018-06-15 14:31
 **/
public class LogConsumer {
    private final static Logger logger = LoggerFactory.getLogger(LogConsumer.class);
    private final static String CONSUMER_CONFIG_FILE = "consumer.properties";

    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            props.load(LogConsumer.class.getClassLoader().getResourceAsStream(CONSUMER_CONFIG_FILE));
        } catch (IOException e) {
            logger.error("Failed to load the config file of kafka consumer!");
        }

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        Collection<String> topics = Arrays.asList(props.getProperty("topics").split(","));
        consumer.subscribe(topics);
        int i=0;
        long s = System.currentTimeMillis();
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        // 批量提交数量
        final int minBatchSize = 1000;
        while(true) {
            ConsumerRecords<String, String> records = consumer.poll(
                    Long.parseLong(props.getProperty("poll.timeout", "3000")));
            if(!records.isEmpty()) {
                for(ConsumerRecord<String, String> record : records) {
//                    logger.info("key = " + record.key() + ", value = " + record.value());
                    buffer.add(record);
                    i++;
                }
                if(buffer.size() > minBatchSize) {
                    consumer.commitAsync();
                    buffer.clear();
                }
            } else {
                logger.info("consume {} messags, cost: {}ms, heartbeat...", i, System.currentTimeMillis() -s);
            }
        }
    }
}
