package jettySpring.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-3-21
 * Time: 下午5:03
 * To change this template use File | Settings | File Templates.
 */
public class MyServer {
    public static void main(String[] args) throws Exception {
        new ClassPathXmlApplicationContext("spring.xml");
    }
}
