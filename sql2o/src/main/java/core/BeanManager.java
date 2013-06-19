package core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-6-18
 * Time: 上午10:53
 * To change this template use File | Settings | File Templates.
 */
public final class BeanManager {
    private static ApplicationContext context;

    static {
        init();
    }

    private static synchronized void init() {
        context = new ClassPathXmlApplicationContext("classpath:spring.xml");
    }

    public static ConnectionPoolDataSource getDruidDataSource() {
        return context.getBean("druidDataSource", ConnectionPoolDataSource.class);
    }
}
