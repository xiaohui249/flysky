package executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-6-21
 * Time: 下午6:49
 * To change this template use File | Settings | File Templates.
 */
public class ExecutorTools {

    private final static Logger log = LoggerFactory.getLogger(ExecutorTools.class);

    private final static ExecutorService pool = Executors.newCachedThreadPool();

    public static void execute(Runnable runnable, int num) {

        long s = System.currentTimeMillis();

        for(int i=0; i<num; i++) {
            pool.execute(runnable);
        }

        log.info(num + " threads has run, it costs " + (System.currentTimeMillis() - s) + "ms.");

        pool.shutdown();

    }
}
