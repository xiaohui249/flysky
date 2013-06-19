package core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;

import javax.sql.ConnectionPoolDataSource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-6-18
 * Time: 上午10:49
 * To change this template use File | Settings | File Templates.
 */
public class Executor {

    private final static Logger logger = LoggerFactory.getLogger(Executor.class);

    private static Sql2o sql2o;

    static {
        if(sql2o == null) {
            init();
        }
    }

    private static synchronized void init() {
        if(sql2o == null) {
            ConnectionPoolDataSource druidDataSource = BeanManager.getDruidDataSource();
            if(druidDataSource != null) {
                sql2o = new Sql2o(druidDataSource);
            }else{
                logger.error("Get ConnectionPoolDataSource failed!");
                System.exit(0);
            }
        }
    }

    public static Sql2o getSql2o() {
        return sql2o;
    }
}
