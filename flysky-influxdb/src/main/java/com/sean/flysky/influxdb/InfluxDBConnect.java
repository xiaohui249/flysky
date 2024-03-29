package com.sean.flysky.influxdb;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaoh
 * @create 2019-04-09 16:34
 **/
public class InfluxDBConnect {
    private String username;//用户名
    private String password;//密码
    private String openurl;//连接地址
    private String database;//数据库

    private InfluxDB influxDB;


    public InfluxDBConnect(String username, String password, String openurl, String database){
        this.username = username;
        this.password = password;
        this.openurl = openurl;
        this.database = database;
    }

    /**连接时序数据库；获得InfluxDB**/
    public InfluxDB  influxDbBuild(){
        if(influxDB == null){
            influxDB = InfluxDBFactory.connect(openurl, username, password);
            influxDB.createDatabase(database);

        }
        return influxDB;
    }

    /**
     * 设置数据保存策略
     * defalut 策略名 /database 数据库名/ 30d 数据保存时限30天/ 1  副本个数为1/ 结尾DEFAULT 表示 设为默认的策略
     */
    public void createRetentionPolicy(){
        String command = String.format("CREATE RETENTION POLICY \"%s\" ON \"%s\" DURATION %s REPLICATION %s DEFAULT",
                "defalut", database, "30d", 1);
        this.query(command);
    }

    /**
     * 查询
     * @param command 查询语句
     * @return
     */
    public QueryResult query(String command){
        return influxDB.query(new Query(command, database));
    }

    /**
     * 插入
     * @param measurement 表
     * @param tags 标签
     * @param fields 字段
     */
    public void insert(String measurement, Map<String, String> tags, Map<String, Object> fields){
        Point.Builder builder = Point.measurement(measurement);
        builder.tag(tags);
        builder.fields(fields);

        influxDB.write(database, "", builder.build());
    }

    /**
     * 删除
     * @param command 删除语句
     * @return 返回错误信息
     */
    public String deleteMeasurementData(String command){
        QueryResult result = influxDB.query(new Query(command, database));
        return result.getError();
    }

    /**
     * 创建数据库
     * @param dbName
     */
    public void createDB(String dbName){
        influxDB.createDatabase(dbName);
    }

    public InfluxDB getInfluxDB() {
        return influxDB;
    }

    public Point convertToPoint(Object obj) throws Exception {
        Class clz = obj.getClass();
        String name = ((Measurement) clz.getAnnotation(Measurement.class)).name();
        Point.Builder builder = Point.measurement(name);
        Field[] fields = clz.getDeclaredFields();
        Map<String, Object> fieldMaps = new TreeMap();
        for (Field field : fields){
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Column column =  field.getAnnotation(Column.class);
            if(column != null){
                Object value = field.get(obj);
                if(value==null)continue;
                if ("time".equals(column.name())) {
//                    Instant timeT = Instant.parse((String)value);
                    builder.time(((Instant) value).toEpochMilli(), TimeUnit.MILLISECONDS);
                } else {
                    if (column.tag()) {
                        builder.tag(column.name(), String.valueOf(value));
                    } else {
                        fieldMaps.put(column.name(),value);
                    }
                }
            }
        }
        builder.fields(fieldMaps);
        Point point = builder.build();
        return point;
    }

    /**
     * 删除数据库
     * @param dbName
     */
    public void deleteDB(String dbName){
        influxDB.deleteDatabase(dbName);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpenurl() {
        return openurl;
    }

    public void setOpenurl(String openurl) {
        this.openurl = openurl;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
