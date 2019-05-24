package com.sean.flysky.es.transport;

import com.alibaba.fastjson.JSONObject;
import com.sean.flysky.es.entity.ItmTranDeltFactory;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @author xiaoh
 * @create 2019-04-24 14:51
 **/
public class ESUtilTest {

    private static Logger logger = LoggerFactory.getLogger(ESUtilTest.class);

    private static String indexName = "trans-" + new DateTime().toString("yyyyMMdd");
    private static String typeName = "transaction";

    public static void main(String[] args) {

//        createIndex(indexName);

//        testAdd();
//        testBatchAdd();

        logger.info(ItmTranDeltFactory.createJson().toJSONString());
    }

    public static void createIndex(String indexName) {
        // 1.判定索引是否存在
        boolean flag = ESUtil.isExists(indexName);
        logger.info("isExists: {}", flag);

        if(!flag) {
            // 2.创建索引
            flag = ESUtil.createIndex(indexName, 3, 0);
            logger.info("createIndex: {}", flag);
        }
    }

    public static void createMapping() {
        // 3.设置Mapping
        try {
            XContentBuilder builder = jsonBuilder()
                    .startObject()
                    .startObject("properties")
                    .startObject("id")
                    .field("type", "long")
                    .endObject()
                    .startObject("title")
                    .field("type", "text")
                    .field("analyzer", "ik_max_word")
                    .field("search_analyzer", "ik_max_word")
                    .field("boost", 2)
                    .endObject()
                    .startObject("content")
                    .field("type", "text")
                    .field("analyzer", "ik_max_word")
                    .field("search_analyzer", "ik_max_word")
                    .endObject()
                    .startObject("postdate")
                    .field("type", "date")
                    .field("format", "yyyy-MM-dd HH:mm:ss")
                    .endObject()
                    .startObject("url")
                    .field("type", "keyword")
                    .endObject()
                    .endObject()
                    .endObject();
            boolean flag = ESUtil.setMapping("index1", "blog", builder);
            logger.info("put mapping: {}", flag);
        } catch (IOException e) {
            logger.error("Mapping创建失败！", e);
        }
    }

    public static void testAdd() {
        JSONObject json = ItmTranDeltFactory.createJson();
        String id = ESUtil.add(indexName, typeName, json);
        logger.info("id = {}, json: {}", id, json.toJSONString());
    }

    public static void testBatchAdd() {
        List<JSONObject> jsonObjectList = new ArrayList<>(999);
        for(int i=0; i<999; i++) {
            jsonObjectList.add(ItmTranDeltFactory.createJson());
        }
        int status = ESUtil.batchAddJSONObject(indexName, typeName, jsonObjectList);
        logger.info("execute batch add, status: {}", status);
    }

}
