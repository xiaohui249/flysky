package log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-10-14
 * Time: 下午3:05
 * SLF4J + Log4J
 */
public class Slf4jLog {
    private static final Logger logger = LoggerFactory.getLogger(Slf4jLog.class);
    public static void main(String[] args) {
        logger.debug( " 111 " );
        logger.info( " 222 " );
        logger.warn( " 333 " );
        logger.error( " 444 " );
    }
}
