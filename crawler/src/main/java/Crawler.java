import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-6-27
 * Time: 下午6:15
 * To change this template use File | Settings | File Templates.
 */
public class Crawler implements Runnable {

    private static BlockingQueue<String> queue = new LinkedBlockingQueue<String>(1000);

    @Override
    public void run() {
        while(true) {
            try {
                String url = queue.take();

            }catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}
