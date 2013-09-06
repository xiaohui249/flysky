package client;

import com.google.common.collect.ImmutableMap;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.Message;
import kafka.message.MessageAndMetadata;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-9-6
 * Time: 上午10:45
 * To change this template use File | Settings | File Templates.
 */
public class KafkaConsumer {
    public static void main(String[] args) {
        // specify some consumer properties
        Properties props = new Properties();
        props.put("zk.connect", "10.1.156.198:2181");
        props.put("zk.connectiontimeout.ms", "1000000");
        props.put("groupid", "test_group");

        // Create the connection to the cluster
        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);

        // create 4 partitions of the stream for topic “test”, to allow 4 threads to consume
        Map<String, List<KafkaStream<Message>>> topicMessageStreams =
                consumerConnector.createMessageStreams(ImmutableMap.of("test", 4));
        List<KafkaStream<Message>> streams = topicMessageStreams.get("test");

        // create list of 4 threads to consume from each of the partitions
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // consume the messages in the threads
        for(final KafkaStream<Message> stream: streams) {
            executor.submit(new Runnable() {
                public void run() {
                    for(MessageAndMetadata msgAndMetadata: stream) {
                        Message message = (Message) msgAndMetadata.message();
                        System.out.println(Thread.currentThread().getName() + " : " + new String(message.payload().array()));
                    }
                }
            });
        }

    }
}
