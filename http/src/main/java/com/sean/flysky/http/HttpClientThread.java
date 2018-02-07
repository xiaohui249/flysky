package com.sean.flysky.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-8-28
 * Time: 下午6:18
 * HTTP 客户端线程，以供测试HTTP服务
 */
public class HttpClientThread implements Runnable {

    private String url;
    private String type = "GET";
    private String param = "";

    public HttpClientThread(String url) {
        this.url = url;
    }

    public HttpClientThread(String url, String param) {
        this(url);
        this.param = param;
    }

    public HttpClientThread(String url, String type, String param) {
        this(url, param);
        this.type = type;
    }

    @Override
    public void run() {
        if(url == null) {
            System.out.println("URL不能为空！");
        }else{
            HttpClient httpClient = new DefaultHttpClient();
            HttpRequestBase request = null;

            if(type.equalsIgnoreCase("GET")) {
                request = new HttpGet(url + (param.equals("") ? "" : "?"+param));
            }else if(type.equalsIgnoreCase("POST")) {
                request = new HttpPost(url);
            }

            if(request != null) {
                InputStream instream = null;
                try {
                    HttpResponse response = httpClient.execute(request);
                    HttpEntity entity = response.getEntity();
                    if(response != null) {
                        instream = entity.getContent();
                        BufferedReader reader = new BufferedReader( new InputStreamReader(instream));
                        System.out.println(reader.readLine());
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        // Closing the input stream will trigger connection release
                        if(instream != null) instream.close();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpClient.getConnectionManager().shutdown();
        }
    }
}
