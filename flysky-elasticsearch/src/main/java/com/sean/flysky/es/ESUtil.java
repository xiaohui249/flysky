package com.sean.flysky.es;


import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * ESUtil
 *
 * @author xiaoh
 * @create 2019-04-24 14:39
 **/
public class ESUtil {
    //集群名,默认值elasticsearch
    private static final String CLUSTER_NAME = "es-cluster";
    //ES集群中某个节点
    private static final String HOSTNAME = "centos7-1";
    //连接端口号
    private static final int TCP_PORT = 9300;
    //构建Settings对象
    private static Settings settings = Settings.builder().put("cluster.name", CLUSTER_NAME).build();
    //TransportClient对象，用于连接ES集群
    private static volatile TransportClient client;

    public static TransportClient getClient(){
        if(client == null){
            synchronized (TransportClient.class){
                try {
                    client = new PreBuiltTransportClient(settings)
                            .addTransportAddress(new TransportAddress(InetAddress.getByName(HOSTNAME), TCP_PORT));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        return client;
    }

    /**
     * 获取索引管理的IndicesAdminClient
     */
    public static IndicesAdminClient getAdminClient() {
        return getClient().admin().indices();
    }

    /**
     * 判定索引是否存在
     * @param indexName
     * @return
     */
    public static boolean isExists(String indexName){
        IndicesExistsResponse response=getAdminClient().prepareExists(indexName).get();
        return response.isExists() ? true : false;
    }
    /**
     * 创建索引
     * @param indexName
     * @return
     */
    public static boolean createIndex(String indexName){
        CreateIndexResponse createIndexResponse = getAdminClient()
                .prepareCreate(indexName.toLowerCase())
                .get();
        return createIndexResponse.isAcknowledged() ? true : false;
    }

    /**
     * 创建索引
     * @param indexName 索引名
     * @param shards   分片数
     * @param replicas  副本数
     * @return
     */
    public static boolean createIndex(String indexName, int shards, int replicas) {
        Settings settings = Settings.builder()
                .put("index.number_of_shards", shards)
                .put("index.number_of_replicas", replicas)
                .build();
        CreateIndexResponse createIndexResponse = getAdminClient()
                .prepareCreate(indexName.toLowerCase())
                .setSettings(settings)
                .execute().actionGet();
        return createIndexResponse.isAcknowledged() ? true : false;
    }

    /**
     * 位索引indexName设置mapping
     * @param indexName
     * @param typeName
     * @param mapping
     */
    public static boolean setMapping(String indexName, String typeName, String mapping) {
        PutMappingResponse putMappingResponse = getAdminClient().preparePutMapping(indexName)
                .setType(typeName)
                .setSource(mapping, XContentType.JSON)
                .get();
        return putMappingResponse.isAcknowledged() ? true : false;
    }

    public static boolean setMapping(String indexName, String typeName, XContentBuilder sources) {
        PutMappingResponse putMappingResponse = getAdminClient().preparePutMapping(indexName)
                .setType(typeName)
                .setSource(sources)
                .get();
        return putMappingResponse.isAcknowledged() ? true : false;
    }

    /**
     * 删除索引
     * @param indexName
     * @return
     */
    public static boolean deleteIndex(String indexName) {
        DeleteIndexResponse deleteResponse = getAdminClient()
                .prepareDelete(indexName.toLowerCase())
                .execute()
                .actionGet();
        return deleteResponse.isAcknowledged() ? true : false;
    }

    /**
     * 将文档内容写入指定的index，type中
     * @param indexName
     * @param typeName
     * @param sources
     * @return 返回文档id
     */
    public static String add(String indexName, String typeName, XContentBuilder sources) {
        IndexResponse indexResponse = getClient().prepareIndex(indexName, typeName)
                .setSource(sources)
                .get();
        return indexResponse.getId();
    }

    public static String add(String indexName, String typeName, JSONObject json) {
        IndexResponse indexResponse = getClient().prepareIndex(indexName, typeName)
                .setSource(json.toJSONString(), XContentType.JSON)
                .get();
        return indexResponse.getId();
    }

    /**
     * 将文档内容写入指定的index，type中, 并指定id
     * @param indexName
     * @param typeName
     * @param source
     * @return 返回文档id
     */
    public static String add(String indexName, String typeName, String id, XContentBuilder source) {
        IndexResponse indexResponse = getClient().prepareIndex(indexName, typeName, id)
                .setSource(source)
                .get();
        return indexResponse.getId();
    }

    /**
     * 批量将文档内容写入指定的index, type中
     * @param indexName
     * @param typeName
     * @param sources
     * @return 返回执行状态码
     */
    public static int batchAdd(String indexName, String typeName, List<XContentBuilder> sources) {
        BulkRequestBuilder bulkRequestBuilder = getClient().prepareBulk();
        for(XContentBuilder source : sources) {
            bulkRequestBuilder.add(getClient().prepareIndex(indexName, typeName).setSource(source));
        }
        BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
        return bulkResponse.status().getStatus();
    }

    public static int batchAddJSONObject(String indexName, String typeName, List<JSONObject> sources) {
        BulkRequestBuilder bulkRequestBuilder = getClient().prepareBulk();
        for(JSONObject source : sources) {
            bulkRequestBuilder.add(getClient().prepareIndex(indexName, typeName).setSource(source.toJSONString(), XContentType.JSON));
        }
        BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
        return bulkResponse.status().getStatus();
    }

}
