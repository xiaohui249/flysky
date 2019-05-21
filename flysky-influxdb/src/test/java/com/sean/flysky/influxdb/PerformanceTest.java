package com.sean.flysky.influxdb;

import com.sean.flysky.ItmTranDeltFactory;
import com.sean.flysky.pojo.ItmTranDelt;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 性能测试
 *
 * @author xiaoh
 * @create 2019-04-11 12:16
 **/
public class PerformanceTest {

    private static Logger logger = LoggerFactory.getLogger(PerformanceTest.class);

    private InfluxDBConnect influxDB;
    private String username = "test";//用户名
    private String password = "123456";//密码
    private String openurl = "http://localhost:8086";//连接地址
    private String database = "test";//数据库
    private String measurement = "trans";

    private final static int TOTAL = 500000;
    private final static int BATCH_SIZE = 5000;

    @Before
    public void setUp(){
        //创建 连接
        influxDB = new InfluxDBConnect(username, password, openurl, database);

        influxDB.influxDbBuild();

        influxDB.createRetentionPolicy();
    }

    @Test
    public void batchInsert() throws Exception {
        List<String> records = new ArrayList<>(TOTAL);
        long s = System.currentTimeMillis();
        for(int i=0; i<TOTAL; i++) {
            ItmTranDelt itmTranDelt = ItmTranDeltFactory.create();
            Point point = influxDB.convertToPoint(itmTranDelt);
            records.add(point.lineProtocol());
        }
        influxDB.getInfluxDB().write(database, "", InfluxDB.ConsistencyLevel.ALL, records);
        logger.info("batch insert data size: {}, cost {}ms", TOTAL , (System.currentTimeMillis()-s));
    }

    @Test
    public void insert() throws Exception {
        ItmTranDelt itmTranDelt;
        long s = System.currentTimeMillis();
        for(int i=0; i<TOTAL; i++) {
            itmTranDelt = ItmTranDeltFactory.create();
            Point point = influxDB.convertToPoint(itmTranDelt);
            influxDB.getInfluxDB().write(database, "", point);
        }
        logger.info("insert data size: {}, cost {}ms", TOTAL, (System.currentTimeMillis()-s));
    }

    @Test
    public void continueBatchInsert() throws Exception {
        List<String> records = new ArrayList<>(BATCH_SIZE);
        long s = System.currentTimeMillis();
        for(int i=0; i<TOTAL; i++) {
            ItmTranDelt itmTranDelt = ItmTranDeltFactory.create();
            Point point = influxDB.convertToPoint(itmTranDelt);
            records.add(point.lineProtocol());
            Thread.sleep(1);
            if(records.size() == BATCH_SIZE) {
                influxDB.getInfluxDB().write(database, "", InfluxDB.ConsistencyLevel.ALL, records);
                logger.info("insert {} records success", records.size());
                records.clear();
            }
        }
        logger.info("batch insert data size: {}, cost {}ms", TOTAL , (System.currentTimeMillis()-s));
    }

//    @Ignore("don't execute this method")
//    @After
//    public void clean() {
//        influxDB.deleteDB(database);
//    }

}
