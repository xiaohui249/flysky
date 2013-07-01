import model.DocumentObj;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import regex.RegExpValidator;
import sequence.GenerateSequenceUtil;

import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-6-27
 * Time: 下午6:15
 * To change this template use File | Settings | File Templates.
 */
public class Crawler implements Runnable {

    private final static Logger log = LoggerFactory.getLogger(Crawler.class);

    private static BlockingQueue<String> waitors = new LinkedBlockingQueue<String>(10000);
    private static Set<String> overs = new HashSet<String>();

    public Crawler() {}
    public Crawler(String url) {
        try {
            waitors.put(url);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true) {
            try {

                log.info(Thread.currentThread().getName() + " has " + waitors.size() + " waitors.");

                String url = waitors.take();

                if(isOver(url)) {   //如果处理过，则跳过
                    continue;
                }

                log.info(Thread.currentThread().getName() + " starts handle : " + url);

                String[] pageInfo = Parser.getContent(url);
                DocumentObj dobj = null;
                if(pageInfo != null) {
                    dobj = parse(pageInfo[1], url, pageInfo[0]);

                    //放入数据库
                    DocumentDao.insert(dobj);
                }

                addToOver(url);

                if(dobj != null) {
                    log.info(Thread.currentThread().getName() + " " + dobj.toString());
                }

                Thread.sleep(1000);

            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public DocumentObj parse(String html, String url, String charset) {

        Document doc = Jsoup.parse(html);

        Elements titles = doc.select("head > title");
        if(titles.isEmpty()) {  //如果文章没有标题，则抛弃不予处理
            return null;
        }

        //处理文章标题
        DocumentObj documentObj = new DocumentObj();
        documentObj.setId(Long.parseLong(GenerateSequenceUtil.generateSequenceNo()));
        documentObj.setUrl(url);

        String title = titles.get(0).text();
        documentObj.setTitle(title);

        //处理文章摘要
        Elements desces = doc.select("head > meta[name=description]");
        String desc = "";
        if(!desces.isEmpty()) {
            desc = desces.get(0).attr("content");
        }
        documentObj.setDesc(desc);

        //处理连接
        Elements links = doc.select("a[href]");
        log.info("links : " + links.size());
        try {
            for(Element link : links) {
                String lk = link.attr("abs:href");

                if(RegExpValidator.IsUrl(lk) && lk.startsWith(url)) {
                    log.info("======link: " + lk);
//                    waitors.put(lk);
                    waitors.offer(lk, 1000, TimeUnit.MILLISECONDS);
                }
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        return documentObj;
    }

    public synchronized boolean isOver(String url) {
        return overs.contains(url);
    }

    public synchronized void addToOver(String url) {
        overs.add(url);
    }

}
