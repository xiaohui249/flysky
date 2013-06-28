import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-6-27
 * Time: 下午6:38
 * To change this template use File | Settings | File Templates.
 */
public class Parser {

    private final static Logger log = LoggerFactory.getLogger(Parser.class);

    private static HttpClient httpClient = new DefaultHttpClient();

    /**
     * 获取url地址的内容
     * @param url
     * @return 如果请求失败或者标题不存在，则返回空字符串。
     */
    public static String[] getContent(String url) {
        String[] result = null;
        StringBuffer sb = new StringBuffer();
        HttpGet get = new HttpGet(url);

        try {
            HttpResponse response = httpClient.execute(get);

            int code = response.getStatusLine().getStatusCode();
            if(code == 200) {
                HttpEntity entity = response.getEntity();

                if(entity != null) {
                    result = new String[2];

                    Header header = entity.getContentEncoding();
                    String encode = null;
                    if(header != null) {
                        encode = header.getValue();
                    }else{
                        encode = getEncode(url);
                    }
                    encode = (encode == null) ? "utf-8" : encode;
                    result[0] = encode;

                    InputStream inputStream = entity.getContent();
                    byte[] buf = new byte[1024 * 12];
                    String temp;
                    while(inputStream.read(buf) > 0) {
                        temp = new String(buf, encode);
                        sb.append(temp);
                    }
                    result[1] = sb.toString();

                }
            }
        }catch (ClientProtocolException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getEncode(String url) {
        String encode = null;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements es = doc.select("head > meta[http-equiv=Content-Type]");
            if(es.isEmpty()){
                return encode;
            }
            StringTokenizer tokenizer = new StringTokenizer(es.get(0).attr("content"), ";");
            while(tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                if(token.toLowerCase().indexOf("charset=") != -1) {
                    encode = token.split("=")[1];
                    break;
                }
            }
            es.get(0).attr("content");
        }catch (IOException e) {
            e.printStackTrace();
        }
        return encode;
    }

    public static void main(String[] args) {
        String[] info = getContent("http://www.sina.com.cn/");
        log.info(info[1]);
    }
}
