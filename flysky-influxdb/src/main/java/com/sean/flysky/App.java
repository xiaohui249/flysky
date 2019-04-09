package com.sean.flysky;

import com.sean.flysky.influxdb.InfluxDBConnection;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        InfluxDBConnection influxDBConnection = new InfluxDBConnection("test", "123", "http://10.7.3.88:8086", "test", "");
        Map<String, String> tags = new HashMap<>();
        tags.put("tag1", "标签值");
        Map<String, Object> fields = new HashMap<>();
        fields.put("field1", "String类型");
        // 数值型，InfluxDB的字段类型，由第一天插入的值得类型决定
        fields.put("field2", 3.141592657);
        // 时间使用毫秒为单位
        influxDBConnection.insert("t1", tags, fields, System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }
}
