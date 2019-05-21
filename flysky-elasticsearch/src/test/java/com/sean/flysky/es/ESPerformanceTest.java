package com.sean.flysky.es;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.transport.Transport;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * ES性能测试类
 *
 * @author xiaoh
 * @create 2019-04-24 17:53
 **/
public class ESPerformanceTest {

    private static Logger logger = LoggerFactory.getLogger(ESPerformanceTest.class);

    private String indexName = "trans-" + new DateTime().toString("yyyyMMdd");
    private String typeName = "transaction";

    private int total = 100000;
    private int batchSize = 2000;

    private TransportClient client;

    @Before
    public void setUp() {
        client = ESUtil.getClient();
        ESUtil.createIndex(indexName, 1, 0);
    }

    @Test
    public void testAdd() {
        long s = System.currentTimeMillis();
        for(int i=0; i<total; i++) {
            ESUtil.add(indexName, typeName, ItmTranDeltFactory.createJson());
        }
        logger.info("add size = {}, cost: {}ms", total, (System.currentTimeMillis() - s));
    }

    @Test
    public void testBatchAdd() {
        List<JSONObject> list = new ArrayList<>(batchSize);
        long s = System.currentTimeMillis();
        for(int i=0; i<total; i++) {
            list.add(ItmTranDeltFactory.createJson());
            if(list.size() == batchSize) {
                int status = ESUtil.batchAddJSONObject(indexName, typeName, list);
                if(status == 200) {
                    list.clear();
                }else {
                    Assert.fail("批量索引失败！");
                    break;
                }
            }
        }
        logger.info("add size = {}, cost: {}ms", total, (System.currentTimeMillis() - s));
    }

    @After
    public void clean() {
//        ESUtil.deleteIndex(indexName);
        client.close();
    }

}
