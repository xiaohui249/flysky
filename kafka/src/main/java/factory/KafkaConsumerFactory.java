package factory;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.Message;
import kafka.message.MessageAndMetadata;
import util.PropertyUtil;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-9-10
 * Time: 上午10:48
 * Kafka consumer factory, it could consume message from kafka
 */
public class KafkaConsumerFactory {

    private static Properties props;
    private static ConsumerConnector consumer;

    static {
        props = PropertyUtil.load("kafka.properties");
    }

    private static void initConsumer(String groupId) {
        props.setProperty("groupid", groupId);
        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        consumer = Consumer.createJavaConsumerConnector(consumerConfig);
    }

    /**
     * 消费方法
     * @param topic 指定消费Topic
     * @param groupId 指定消费组
     * @param worker 指定消费线程数
     */
    public static void consume(String topic, String groupId, int worker, final Function<byte[], Boolean> handler) {

        //初始化consumer
        initConsumer(groupId);

        if(consumer == null) {
            System.out.println("consumer init failed!");
            System.exit(0);
        }

        // create 4 partitions of the stream for topic “topic”, to allow "worker" threads to consume
        Map<String, List<KafkaStream<Message>>> topicMessageStreams =
                consumer.createMessageStreams(ImmutableMap.of(topic, worker));
        List<KafkaStream<Message>> streams = topicMessageStreams.get(topic);

        // create list of "worker" threads to consume from each of the partitions
        ExecutorService executor = Executors.newFixedThreadPool(worker);

        // consume the messages in the threads
        for(final KafkaStream<Message> stream: streams) {
            executor.submit(new Runnable() {
                public void run() {
                    for(MessageAndMetadata msgAndMetadata: stream) {
                        Message message = (Message) msgAndMetadata.message();
                        handler.apply(getMessage(message));
                    }
                }
            });
        }
    }

    private static byte[] getMessage(Message message) {
        ByteBuffer buffer = message.payload();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return bytes;
    }

    public static void main(String[] args) {
        consume("sean", "test1", 3, new Function<byte[], Boolean>() {
            @Override
            public Boolean apply(byte[] bytes) {
                System.out.println(Thread.currentThread().getName() + " : " + new String(bytes));
                return true;
            }
        });
    }

}
