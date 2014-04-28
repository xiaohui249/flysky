import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * Created by bbgds on 14-3-6.
 */
public class JsonUtil {
    /**
     * 实现将实体对象转换成json对象
     * @param medicine Medicine对象
     * @return
     */
    public static String obj2JsonData(Medicine medicine){
        String jsonData = null;
        try {
            //使用XContentBuilder创建json数据
            XContentBuilder jsonBuild = XContentFactory.jsonBuilder();
            jsonBuild.startObject()
                    .field("id",medicine.getId())
                    .field("name", medicine.getName())
                    .field("funciton",medicine.getFunction())
                    .field("num", medicine.getNum())
                    .endObject();
            jsonData = jsonBuild.string();
            System.out.println(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonData;
    }
}
