package com.sean.flysky;

import in.zapr.druid.druidry.client.DruidClient;
import in.zapr.druid.druidry.client.DruidConfiguration;
import in.zapr.druid.druidry.client.DruidJerseyClient;
import in.zapr.druid.druidry.query.DruidQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MyDruidClient
 *
 * @author xiaoh
 * @create 2018-06-29 17:06
 **/
public class MyDruidClient {
    private final static Logger logger = LoggerFactory.getLogger(MyDruidClient.class);

    private DruidClient client;

    public MyDruidClient(String host, Integer port) {
        DruidConfiguration configuration = DruidConfiguration.builder()
                .host(host).port(port).endpoint("druid/v2/").build();
        client = new DruidJerseyClient(configuration);
    }

    public String query(DruidQuery query) throws Exception {
        client.connect();
        String result = client.query(query);
        client.close();
        return result;
    }

}
