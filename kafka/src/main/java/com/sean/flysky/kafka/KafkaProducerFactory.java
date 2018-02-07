package com.sean.flysky.kafka;

import akka.actor.*;
import akka.routing.RoundRobinRouter;
import kafka.javaapi.producer.Producer;
import kafka.javaapi.producer.ProducerData;
import kafka.message.Message;
import kafka.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sean.flysky.kafka.util.PropertyUtil;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
* Created with IntelliJ IDEA.
* User: huixiao200068
* Date: 13-9-10
* Time: 上午9:56
* Kafka producer factory, it could send message to kafka
*/
public class KafkaProducerFactory {

    private final static Logger logger = LoggerFactory.getLogger(KafkaProducerFactory.class);
    private static final int THRESHOLD = 5; //统计阈值，每10000条打印输出一次统计信息
    private static final int WORKER_NUM = 1;

    private static Properties props;
    private static DataWrapper wrapper;
    private static Producer<String, Message> producer;
    private final static ActorRef master;

    static {
        //初始化Producer
        props = PropertyUtil.load("kafka.properties");
        ProducerConfig producerConfig = new ProducerConfig(props);
        producer = new Producer<String, Message>(producerConfig);

        //初始化Actor
        ActorSystem system = ActorSystem.create("TailSystem");
        final ActorRef stat = system.actorOf(new Props(Stat.class), "tail-stat");
        master = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new Master(WORKER_NUM, stat);
            }
        }), "master");
    }


    public static void send(String topic, byte[] msg) {
        if(wrapper == null) {
            wrapper = new DataWrapper(topic);
        }
        master.tell(msg, master);
    }

    public static void sendDefault(String topic, byte[] msg) {
        if(wrapper == null) {
            wrapper = new DataWrapper(topic);
        }
        Message message = new Message(msg);
        producer.send(wrapper.wrap(message));

        System.out.println("inserted: " + new String(msg));
    }

    private static class DataWrapper {

        private final String topic;

        public DataWrapper(String topic) {
            this.topic = topic;
        }

        public ProducerData<String, Message> wrap(Message msg) {
            return new ProducerData<String, Message>(topic, msg);
        }

    }

    /**
     * 主Actor，处理流程的开始者，负责读取log数据，往下传递事件。
     */
    public static class Master extends UntypedActor {

        private final ActorRef workerRouter;

        public Master(final int workNum, final ActorRef stat) {
            Props props = new Props(new UntypedActorFactory() {
                @Override
                public Actor create() {
                    return new Worker(stat);
                }
            });
            workerRouter = this.getContext().actorOf(props.withRouter(new RoundRobinRouter(workNum)), "workerRouter");
        }

        @Override
        public void onReceive(Object message){
            if(message instanceof byte[]) {
                byte[] bytes = (byte[]) message;
                Message msg = new Message(bytes);
                workerRouter.tell(msg, getSelf());
            }else{
                unhandled(message);
            }

        }
    }

    /**
     * 工作Actor，主要是把处理完成的json形式的日志存入kafka
     */
    public static class Worker extends UntypedActor {

        private AtomicLong counter = new AtomicLong(0);
        private final ActorRef stat;

        public Worker(ActorRef stat) {
            this.stat = stat;
        }

        @Override
        public void onReceive(Object message) throws Exception {
            if(message instanceof  Message) {
                Message msg = (Message) message;
                //写入kafka
                try {
                    producer.send(wrapper.wrap(msg));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //统计存入数据
                Long count = counter.incrementAndGet();
                if(count >= THRESHOLD) {
                    counter.set(0);
                    System.out.println("inserted: " + count);
                    stat.tell(count, getSelf());
                }

            }else{
                unhandled(message);
            }
        }
    }

    /**
     * 统计Actor
     */
    public static class Stat extends UntypedActor {

        private final long ct = System.currentTimeMillis();
        private final AtomicLong total = new AtomicLong(0);

        @Override
        public void onReceive(Object message) {
            if (message instanceof Long) {
                long t = (Long) message;
                long tt = total.addAndGet(t);

                long cc = (System.currentTimeMillis() - ct) / 1000;
                cc = cc > 0 ? cc : 1;
                System.out.println("current total: " + tt + ",time:" + cc + "secs, per/secs: " + (tt / cc));
            } else {
                unhandled(message);
            }
        }

    }

    public static void main(String[] args) {
        for(int i=0; i<10; i++) {
            KafkaProducerFactory.send("sean", ("hello"+i).getBytes());
//            KafkaProducerFactory.sendDefault("sean", ("hello"+i).getBytes());
        }
    }

}
