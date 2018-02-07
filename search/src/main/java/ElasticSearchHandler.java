import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bbgds on 14-3-6.
 */
public class ElasticSearchHandler {
    private Client client;

    public ElasticSearchHandler(){
        //使用本机做为节点
        this("127.0.0.1");
    }

    public ElasticSearchHandler(String ipAddress){
        //集群连接超时设置

//        Settings settings = ImmutableSettings.settingsBuilder().put("com.sean.flysky.netty.tcp.client.transport.ping_timeout", "10s").build();
//        com.sean.flysky.netty.tcp.client = new TransportClient(settings);

        client = new TransportClient().addTransportAddress(new InetSocketTransportAddress(ipAddress, 9300));
    }

    /**
     * 建立索引,索引建立好之后,会在elasticsearch-0.20.6\data\elasticsearch\nodes\0创建所以你看
     * @param indexName 为索引库名，一个es集群中可以有多个索引库。 名称必须为小写
     * @param indexType Type为索引类型，是用来区分同索引库下不同类型的数据的，一个索引库下可以有多个索引类型。
     * @param jsondata json格式的数据集合
     *
     * @return
     */
    public void createIndexResponse(String indexName, String indexType, List<String> jsondata){

        createMappingMedicine(indexName, indexType);

        //创建索引库 需要注意的是.setRefresh(true)这里一定要设置,否则第一次建立索引查找不到数据
        IndexRequestBuilder requestBuilder = client.prepareIndex(indexName, indexType).setRefresh(true);
        for(int i=0; i<jsondata.size(); i++){
            requestBuilder.setSource(jsondata.get(i)).execute().actionGet();
        }
    }

    private void createMappingMedicine(String indexName, String indexType) {
        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder().startObject()
                    .startObject(indexType);
            mapping.startObject("properties");

            mapping.startObject("id").field("type", "integer").field("store", "yes").endObject();

            mapping.startObject("name").field("type", "multi_field").startObject("fields");
            mapping.startObject("_ik").field("type", "string").field("store", "yes").field("analyzer", "ik_max_word").field("term_vector", "yes").endObject();
            mapping.startObject("_standard").field("type", "string").field("store", "yes").field("analyzer", "standard").endObject();
            mapping.endObject();

            mapping.startObject("funciton").field("type", "string").field("store", "yes").endObject();

            mapping.startObject("num").field("type", "integer").field("store", "yes").endObject();

            mapping.endObject().endObject().endObject();

            PutMappingRequest putMappingRequest = Requests.putMappingRequest(indexName)
                    .type(indexType).source(mapping);

            boolean result = client.admin().indices().prepareCreate(indexName).execute().actionGet().isAcknowledged();
            if(result) {
                client.admin().indices().putMapping(putMappingRequest).actionGet().isAcknowledged();
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建索引
     * @param indexName
     * @param type
     * @param jsondata
     * @return
     */
    public IndexResponse createIndexResponse(String indexName, String type,String jsondata){
        IndexResponse response = client.prepareIndex(indexName, type)
                .setSource(jsondata)
                .execute()
                .actionGet();
        return response;
    }

    /**
     * 执行搜索
     * @param queryBuilder
     * @param indexname
     * @param type
     * @return
     */
    public List<Medicine> searcher(QueryBuilder queryBuilder, String indexname, String type){
        List<Medicine> list = new ArrayList<Medicine>();
        SearchResponse searchResponse = client.prepareSearch(indexname).setTypes(type)
                .setQuery(queryBuilder)
                .addHighlightedField("name._ik")
                .addHighlightedField("name._standard")
                .setHighlighterPreTags("<span style=\"color:red\">").setHighlighterPostTags("</span>")
//                .addSort("id", SortOrder.ASC)
                .setExplain(true)
                .execute()
                .actionGet();
        SearchHits hits = searchResponse.getHits();
        System.out.println("查询到记录数=" + hits.getTotalHits());
        SearchHit[] searchHists = hits.getHits();
        if(searchHists.length > 0){
            for(SearchHit hit:searchHists){

                Medicine medicine = new Medicine();

                Integer id = (Integer)hit.getSource().get("id");
                medicine.setId(id);
                System.out.println("id = " + id + ", score = " + hit.getScore() + "\n\r" + hit.explanation());
                String name = (String) hit.getSource().get("name");
                medicine.setName(name);

                Map<String, HighlightField> highField = hit.getHighlightFields();
                // 高亮处理
                if (highField.containsKey("name._ik")) {
                    medicine.setHighName(highField.get("name._ik").fragments()[0].string());
                }
                if (highField.containsKey("name._standard")) {
                    medicine.setHighName(highField.get("name._standard").fragments()[0].string());
                }

                String function = (String) hit.getSource().get("funciton");
                medicine.setFunction(function);
                Integer num = (Integer) hit.getSource().get("num");
                medicine.setNum(num);
                list.add(medicine);
            }
        }
        return list;
    }

    /**
     * 执行搜索
     * @param queryBuilder
     * @param indexname
     * @param type
     * @return
     */
    public void commonSearcher(QueryBuilder queryBuilder, String indexname, String type){

        SearchResponse searchResponse = client.prepareSearch(indexname).setTypes(type)
                .setQuery(queryBuilder)
                .execute()
                .actionGet();
        SearchHits hits = searchResponse.getHits();
        System.out.println("查询到记录数=" + hits.getTotalHits());
        SearchHit[] searchHists = hits.getHits();
        if(searchHists.length > 0){
            for(SearchHit hit:searchHists){
                String id = hit.getSource().get("id").toString();
                String name = hit.getSource().get("name").toString();
                System.out.println("id="+id+",name="+name);
            }
        }

    }


    public static void main(String[] args) {
        String host = "10.201.7.145";
        ElasticSearchHandler esHandler = new ElasticSearchHandler(host);
        String indexname = "indexdemo";
        String type = "typedemo";

//        List<String> jsondata = DataFactory.getInitJsonData();
//        esHandler.createIndexResponse(indexname, type, jsondata);

        //查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//        boolQuery.should(QueryBuilders.queryString("笔记本电脑").field("name._ik").analyzer("ik"));
        boolQuery.should(QueryBuilders.queryString("感冒").field("name._ik").analyzer("ik"));
        List<Medicine> result = esHandler.searcher(QueryBuilders.functionScoreQuery(boolQuery)
                .add(FilterBuilders.rangeFilter("id").lte(1), ScoreFunctionBuilders.scriptFunction("5"))
                , indexname, type);
        for(int i=0; i<result.size(); i++){
            Medicine medicine = result.get(i);
            System.out.println("(" + medicine.getId() + ")药品名称:" +medicine.getHighName() + "\t\t" + medicine.getFunction());
        }
    }

}
