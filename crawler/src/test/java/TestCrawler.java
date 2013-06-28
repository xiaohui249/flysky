import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-6-28
 * Time: 上午11:52
 * To change this template use File | Settings | File Templates.
 */
public class TestCrawler {

    private static ExecutorService service = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        service.execute(new Crawler("http://news.baidu.com/"));
//        service.execute(new Crawler());
    }

}
