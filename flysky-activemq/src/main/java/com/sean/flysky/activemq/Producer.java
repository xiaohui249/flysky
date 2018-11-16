package com.sean.flysky.activemq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


/**
 * activemq producer
 *
 * @author xiaoh
 * @create 2018-09-07 18:04
 **/
public class Producer {
    static String NAME = ActiveMQConnection.DEFAULT_USER;   //admin
    static String PSWD = ActiveMQConnection.DEFAULT_PASSWORD;   //admin
    static String URL = "tcp://192.168.20.2:20663";
    static ConnectionFactory connectionFactory;
    static Connection connection;
    static Session session;
    static Queue queue;
    static MessageProducer producer;
    static Integer index = 0;

    public static void main(String[] args) {
        //链接工厂，用来创建链接
        connectionFactory = new ActiveMQConnectionFactory("kayak", "123456", Producer.URL);

        try {
            //创建链接
            connection = connectionFactory.createConnection();
            //启动链接
            connection.start();
            //创建会话
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            //创建列队
            queue = session.createQueue("test");
            //创建生产者，将信息生产到queue中
            producer = session.createProducer(queue);
            //设置生成信息的策略.不序列化，实际看自己的项目业务情况而定，这里只是学习
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            while(index < 10) {
                index++;
                //创建信息
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("name", "曹明杰"+index);
                mapMessage.setString("age", "23");
                //发送信息
                System.out.println("消息已发送");
                producer.send(mapMessage);
                Thread.sleep(3000);
                session.commit();//提交，信息真正的发送
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (null != connection) connection.close(); //关闭
            } catch (Throwable ignore) {
                ignore.printStackTrace();
            }
        }
    }
}
