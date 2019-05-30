package com.sean.flysky.kafka.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * kafka消费者程序示例
 *
 * @author xiaoh
 * @create 2018-06-15 14:31
 **/
public class LogConsumer {
    private final static Logger logger = LoggerFactory.getLogger(LogConsumer.class);
    private final static String CONSUMER_CONFIG_FILE = "consumer.properties";
    private final static ExecutorService executorService = new ThreadPoolExecutor(100, 1000,
            10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000));

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
        final int minBatchSize = Integer.parseInt(props.getProperty("minBatchSize", "1000"));
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(
                        Long.parseLong(props.getProperty("pollTimeout", "3000")));
                if (!records.isEmpty()) {
                    for (ConsumerRecord<String, String> record : records) {
                        // 同步处理，如果处理过程复杂，阻塞offset的提交，整体消费处理性能缓慢，但是数据准确性和同步性较强
                        syncDoWork(record);
                        // 异步处理，无法处理是否成功，都将提交offset，整体消费处理性能快，但是处理失败的数据将丢失
//                        asyncDoWork(record);

                        // 写入内存队列，以便offset提交判断
                        buffer.add(record);
                        i++;
                    }
                    // 如果内存队列数据大于批量提交值，则提交offset
                    if (buffer.size() > minBatchSize) {
                        consumer.commitAsync();
                        buffer.clear();
                    }
                } else {
                    // 超过3秒没有消费到数据，则输出以下心跳日志，同时提交offset
                    logger.info("consume {} messags, cost: {}ms, heartbeat...", i, System.currentTimeMillis() - s);
                    if(buffer.size() > 0) {
                        consumer.commitAsync();
                        buffer.clear();
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步工作方法
     * @param record
     */
    public static void asyncDoWork(final ConsumerRecord<String, String> record) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                logger.info("key = " + record.key() + ", value = " + record.value());
            }
        });
    }

    /**
     * 同步工作方法
     * @param record
     */
    public static void syncDoWork(ConsumerRecord<String, String> record) {
        logger.info("key = " + record.key() + ", value = " + record.value());
    }


}
