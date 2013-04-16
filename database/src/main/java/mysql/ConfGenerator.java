package mysql;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-4-16
 * Time: 下午6:20
 * Tarantool配置文件生成器
 */
public class ConfGenerator {

    final static String configFile = "config.txt";

    public static void main(String[] args) {

    }

    public static void generateSpaceConf(int space, JSONObject indexes, JSONObject fields) {
        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(new File(Const.CONF_DIR + configFile), true));

            StringBuffer sb = new StringBuffer();

            String start = "space[" +space+ "]";
            sb.append(start + ".enable = 1").append(Const.separator);

            Iterator it = indexes.keys();
            int i = 0;
            while(it.hasNext()) {

                String second = start + ".index[" +(i++)+ "]";

                String key = String.valueOf(it.next());
                JSONObject value = indexes.getJSONObject(key);
                JSONArray columns = value.getJSONArray("columns");

                if(columns.length() == 1) {   //如果第2个元素为空，则表示该索引字段数为1，索引类型通常为HASH；否则为TREE
                    sb.append(second + ".type = \"HASH\"").append(Const.separator);
                }else{
                    sb.append(second + ".type = \"TREE\"").append(Const.separator);
                }

                sb.append(second + ".unique = " + (value.getString("Non_unique").equals("0") ? 1 : 0));

                for(int j=0; j<columns.length(); j++) {
                    String three = second + ".key_field[" +j+ "]";

                    String fieldName = columns.getString(j);
                    //TODO:根据元数据解析字段信息
                    String fieldInfo[] = fields.getString(fieldName).split("\\|");
                    sb.append(three + ".fieldno = " + fieldInfo[0]).append(Const.separator);
                    sb.append(three = ".type = \"" +Const.indexType.get(fieldInfo[1])+ "\"").append(Const.separator);

                }

            }
            writer.write(sb.toString());
        }catch (Exception e){
            System.out.println("生成Space配置失败！");
            e.printStackTrace();
        }finally {
            try{
                writer.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
