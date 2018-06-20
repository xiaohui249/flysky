package com.sean.flysky.kafka.demo;

import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * LogProducer
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

        String topic = "mytopic001";
        Producer<String, String> producer = new KafkaProducer<>(props);
        for(int i=1000; i<5000; i++) {
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
//            RecordMetadata metadata = future.get();
//            logger.info("log" + i + ", offset: " + metadata.offset());
        }
        producer.close();
    }
}
