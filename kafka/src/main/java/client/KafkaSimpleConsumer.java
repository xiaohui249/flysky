package client;

import kafka.api.FetchRequest;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.MessageAndOffset;

import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-9-6
 * Time: 上午10:31
 * To change this template use File | Settings | File Templates.
 */
public class KafkaSimpleConsumer {
    public static void main(String[] args) {
        SimpleConsumer consumer = new SimpleConsumer("10.1.156.198", 9092, 10000, 1024000);
        long offset = 0;
        while(true) {
            FetchRequest request = new FetchRequest("test", 0, offset, 1000000);
            ByteBufferMessageSet messages = consumer.fetch(request);
            for(MessageAndOffset message : messages) {
                System.out.println("consumed: " + new String(message.message().payload().array()));
                offset = message.offset();
            }
        }
    }
}
