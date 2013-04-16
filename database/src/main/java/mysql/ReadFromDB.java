package mysql;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.json.JSONObject;

import java.io.File;
import java.sql.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-4-16
 * Time: 上午11:13
 * To change this template use File | Settings | File Templates.
 */
public class ReadFromDB {

    private static AtomicInteger space = new AtomicInteger(1);

    public static void read() {
        Connection conn = DBManager.getConnection("jdbc:mysql://10.10.76.14:3306/smc_user", "portal", "portal@sohu");
        Statement stmt = null;
        ResultSet rs = null;
        try{
            stmt = conn.createStatement();
            String sql = "show tables";
            rs = stmt.executeQuery(sql);
            if(rs != null) {
                while(rs.next()) {
                    System.out.println(rs.getString(1));
                    readTable(rs.getString(1));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                rs.close();
                stmt.close();
                conn.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析数据表
     * @param tablename
     */
    public static void readTable(String tablename) {
        Connection conn = DBManager.getConnection("jdbc:mysql://10.10.76.14:3306/smc_user", "portal", "portal@sohu");
        Statement stmt = null;
        ResultSet rs = null;
        FileWriterWithEncoding writer = null;
        try{
            writer = new FileWriterWithEncoding(new File(Const.DATA_DIR +tablename+".txt"), "utf-8");

            stmt = conn.createStatement();
            String sql = "select * from " + tablename;
            rs = stmt.executeQuery(sql);

            if(rs != null) {
                StringBuffer sb = new StringBuffer();

                ResultSetMetaData metaData =  rs.getMetaData();
                JSONObject fieldsInfo = readMeta(metaData);
                String meta = tablename + "$" + space.incrementAndGet() + "$" +  fieldsInfo.toString() + "$" + getFieldIndex(tablename, fieldsInfo);
                sb.append(meta + Const.separator);

                int count = metaData.getColumnCount();
                while(rs.next()) {
                    StringBuffer temp = new StringBuffer();
                    for(int i=1; i<=count; i++) {
                        temp.append(String.valueOf(rs.getObject(i))).append("$");
                    }
                    sb.append(temp.substring(0, temp.length()-1)).append(Const.separator);
                }

                writer.write(sb.toString());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                writer.close();
                rs.close();
                stmt.close();
                conn.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析元数据
     * @param metaData
     * @return
     */
    public static JSONObject readMeta(ResultSetMetaData metaData) {
        JSONObject json = new JSONObject();
        try{
            int columnCount = metaData.getColumnCount();

            for(int i=1; i<=columnCount; i++) {
                String fieldName = metaData.getColumnName(i);
                int type = metaData.getColumnType(i);
                System.out.println(fieldName + ":" + metaData.getColumnType(i) + ":" + metaData.getColumnTypeName(i));
                json.put(fieldName, (i-1)+"|"+Const.typeMap.get(type));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 获取主键信息
     * @param tablename
     * @return
     */
    public static String getFieldIndex(String tablename, JSONObject fieldsInfo) {
        Connection conn = DBManager.getConnection("jdbc:mysql://10.10.76.14:3306/smc_user", "portal", "portal@sohu");
        Statement stmt = null;
        ResultSet rs = null;
        String index = "";
        try{
            stmt = conn.createStatement();
            String sql = "show index from " + tablename;
            rs = stmt.executeQuery(sql);
            if(rs != null) {
                JSONObject json = new JSONObject();
                while(rs.next()) {
                    String indexName = rs.getString("Key_name");
                    if(json.isNull(indexName)) {
                        JSONObject jn = new JSONObject();
                        jn.put("Non_unique", rs.getString("Non_unique"));
                        jn.append("columns", rs.getString("Column_name"));
                        json.put(indexName, jn);
                    }else{
                        json.getJSONObject(indexName).append("columns",rs.getString("Column_name"));
                    }
                }
                index = json.toString();
//                ConfGenerator.generateSpaceConf(space.intValue(),json, fieldsInfo);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                rs.close();
                stmt.close();
                conn.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return index;
    }

    public static void main(String[] args) {
//        read();
        readTable("tbl_client_android_0");
    }

}
