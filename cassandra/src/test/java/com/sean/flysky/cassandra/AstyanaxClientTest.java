package com.sean.flysky.cassandra;

import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.serializers.LongSerializer;
import com.netflix.astyanax.serializers.StringSerializer;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-8-16
 * Time: 下午2:46
 * To change this template use File | Settings | File Templates.
 */
public class AstyanaxClientTest {

    private static AstyanaxClientFactory factory = AstyanaxClientFactory.buildFactory("Test Cluster", "DEMO", "10.13.80.152", 9160, 1, 5);

    public static void main(String[] args) throws Exception {
//        insertTest();
        getTest();
    }

    public static void insertTest() throws Exception {
        AstyanaxClient client = factory.getClient();

        ColumnFamily data = new ColumnFamily("Data", StringSerializer.get(), LongSerializer.get());
        ColumnFamily keyStamp = new ColumnFamily("KeyStamp", StringSerializer.get(), LongSerializer.get());

        String key = "Android";
        long lastStamp = 0L;
        Random random = new Random();

        int i = 0;
        while(i<100) {
            long ts = System.currentTimeMillis();
            if(lastStamp == 0 || (ts - lastStamp) > 60*1000) {

                // 将当前时间戳作为一个分界点写入KeyStamp中
                client.insert(keyStamp, key, ts, 0);

                lastStamp = ts;

                //将当前记录写入Data中ts
                client.insert(data, key+";"+lastStamp, ts, random.nextInt(1000));

            }else{

                //将记录写入Data中"key;lastStamp"对应的
                client.insert(data, key+";"+lastStamp, ts, random.nextInt(1000));
            }
            i++;
            Thread.sleep(1000);
        }
    }

    public static void getTest() {
        AstyanaxClient client = factory.getClient();

//        ColumnFamily data = new ColumnFamily("Data", StringSerializer.get(), LongSerializer.get());
        ColumnFamily keyStamp = new ColumnFamily("KeyStamp", StringSerializer.get(), LongSerializer.get());

        ColumnList<Long> columns = client.get(keyStamp, "Android", true);
        if(!columns.isEmpty()) {
            for(Column<Long> column : columns) {
                System.out.println(column.getName() + " -> " + column.getIntegerValue());
            }
        }

    }
}
