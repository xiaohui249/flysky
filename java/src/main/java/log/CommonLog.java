package log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-10-14
 * Time: 上午10:59
 * JCL(Jakarta Common Logging) + Log4J
 */
public class CommonLog {
    private static final Log log = LogFactory.getLog(CommonLog.class);
    public static void main(String[] args) {
        log.debug( " 111 " );
        log.info( " 222 " );
        log.warn( " 333 " );
        log.error( " 444 " );
        log.fatal( " 555 " );
    }
}
