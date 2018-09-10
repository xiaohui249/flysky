package com.sean.flysky.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消费者
 *
 * @author xiaoh
 * @create 2018-09-07 18:22
 **/
public class Consumer {
    static String NAME = ActiveMQConnection.DEFAULT_USER;   //admin
    static String PSWD = ActiveMQConnection.DEFAULT_PASSWORD;   //admin
    static String URL = "tcp://192.168.20.2:9885";
    static ConnectionFactory connectionFactory;
    static Connection connection;
    static Session session;
    static Queue queue;
    static MessageConsumer consumer;

    public static void main(String[] args) {
        //创建链接工厂
        connectionFactory = new ActiveMQConnectionFactory(NAME, PSWD, URL);
        try {
            //创建链接
            connection = connectionFactory.createConnection();
            //启动
            connection.start();
            //创建会话
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            //创建目的地即queue
            queue = session.createQueue("test");
            //创建消费者
            consumer = session.createConsumer(queue);
            System.out.println("开始消费");
            while (true) {
                //等待3秒向消息队列中查看是否有消息，有就开始消费
                MapMessage receive = (MapMessage) consumer.receive(3000);
                System.out.println("消费信息为：");
                if (receive != null) {
                    System.out.println(receive.getString("name"));
                    System.out.println(receive.getString("age"));
                } else {
                    System.out.println("heartbeat...");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {
            }
        }
    }
}
