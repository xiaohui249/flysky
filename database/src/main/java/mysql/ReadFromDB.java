package mysql;

import org.apache.commons.io.output.FileWriterWithEncoding;

import java.io.File;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-4-16
 * Time: 上午11:13
 * To change this template use File | Settings | File Templates.
 */
public class ReadFromDB {

    private static AtomicInteger space = new AtomicInteger(0);
    private static String separator = System.getProperty("line.separator", "/n");

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

    public static void readTable(String tablename) {
        Connection conn = DBManager.getConnection("jdbc:mysql://10.10.76.14:3306/smc_user", "portal", "portal@sohu");
        Statement stmt = null;
        ResultSet rs = null;
        FileWriterWithEncoding writer = null;
        try{
            writer = new FileWriterWithEncoding(new File("F:/temp/"+tablename+".txt"), "utf-8");

            stmt = conn.createStatement();
            String sql = "select * from " + tablename;
            rs = stmt.executeQuery(sql);
            if(rs != null) {
                StringBuffer sb = new StringBuffer();

                ResultSetMetaData metaData =  rs.getMetaData();
                String meta = tablename + "$" + space.incrementAndGet() + "$" +  readMeta(metaData) + "$" + getPrimaryIndex(tablename);
                sb.append(meta + separator);

                int count = metaData.getColumnCount();
                while(rs.next()) {
                    StringBuffer temp = new StringBuffer();
                    for(int i=1; i<=count; i++) {
                        temp.append(String.valueOf(rs.getObject(i))).append("$");
                    }
                    sb.append(temp.substring(0, temp.length()-1)).append(separator);
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

    public static String readMeta(ResultSetMetaData metaData) {
        StringBuffer sb = new StringBuffer();
        try{
            int columnCount = metaData.getColumnCount();

            for(int i=1; i<=columnCount; i++) {
                String fieldName = metaData.getColumnName(i);
                int type = metaData.getColumnType(i);
                System.out.println(fieldName+":"+metaData.getColumnType(i));
                sb.append(fieldName).append(":").append(i-1).append("|").append(Const.typeMap.get(type)).append(";");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.substring(0, sb.length()-1);
    }

    public static String getPrimaryIndex(String tablename) {
        Connection conn = DBManager.getConnection("jdbc:mysql://10.10.76.14:3306/smc_user", "portal", "portal@sohu");
        Statement stmt = null;
        ResultSet rs = null;
        String index = "";
        try{
            stmt = conn.createStatement();
            String sql = "show index from " + tablename;
            rs = stmt.executeQuery(sql);
            if(rs != null) {
                while(rs.next()) {
                    String primary = rs.getString("Key_name");
                    if(primary != null && primary.equals("PRIMARY")) {
                        index = rs.getString("Column_name") + ":unique";
                    }
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
        return index;
    }

    public static void main(String[] args) {
//        read();
        readTable("tbl_client_android_0");
    }

}
