import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-3-31
 * Time: 上午12:30
 * To change this template use File | Settings | File Templates.
 */
public class PinyinTest {
    public static void main(String[] args) throws Exception {
        String host = "192.168.2.102";
        ElasticSearchHandler esHandler = new ElasticSearchHandler(host);
        String indexname = "medcl";
        String type = "folks";

//        XContentBuilder jsonBuild = XContentFactory.jsonBuilder();
//        jsonBuild.startObject()
//                .field("id", "tom")
//                .field("name", "韩厚吉")
//                .endObject();
//        String jsonData = jsonBuild.string();
//        IndexResponse response = esHandler.createIndexResponse(indexname, type, jsonData);
//        if(response.isCreated()) {
//            System.out.println("创建成功！");
//        }

        QueryBuilder queryBuilder = QueryBuilders.queryString("h").field("name");
        esHandler.commonSearcher(queryBuilder, indexname, type);
    }
}
