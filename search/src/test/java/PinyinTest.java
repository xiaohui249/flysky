import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

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
