package factory;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.PropertyUtil;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-9-10
 * Time: 上午10:48
 * To change this template use File | Settings | File Templates.
 */
public class KafkaConsumerFactory {

    private final static Logger logger = LoggerFactory.getLogger(KafkaConsumerFactory.class);

    private static Properties props;

    private static ConsumerConnector consumer;

    static {
        props = PropertyUtil.load("kafka.properties");
    }

    public static ConsumerConnector getConsumer(String groupId) {
        Properties p = (Properties) props.clone();
        p.setProperty("groupid", groupId);
        ConsumerConfig consumerConfig = new ConsumerConfig(p);
        consumer = Consumer.createJavaConsumerConnector(consumerConfig);
        return consumer;
    }

}
