package client;

import kafka.javaapi.producer.Producer;
import kafka.javaapi.producer.ProducerData;
import kafka.producer.ProducerConfig;

import java.util.Properties;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-9-5
 * Time: 下午3:30
 * To change this template use File | Settings | File Templates.
 */
public class KafkaProducer {
    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("zk.connect", "10.1.156.198:2181");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);

        String message = getMessage();
        while(!message.equalsIgnoreCase("quit")) {
            ProducerData<String, String> data = new ProducerData<String, String>("test", message);
            producer.send(data);

            message = getMessage();
        }

    }

    public static String getMessage() {
        String message = new Scanner(System.in).nextLine();
        if(message == null) message = "";
        message = message.trim();
        return message;
    }
}
