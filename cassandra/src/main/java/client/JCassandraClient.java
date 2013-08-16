package client;

import org.apache.cassandra.thrift.*;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-8-15
 * Time: 上午10:29
 * Java Client for Cassandra
 */
public class JCassandraClient {

    final static String CODE = "UTF-8";

    public static void main(String[] args) throws TTransportException, UnsupportedEncodingException,
            InvalidRequestException, TException, UnavailableException, TimedOutException, NotFoundException {
        TTransport tr = new TFramedTransport(new TSocket("10.13.80.151", 9160));
        TProtocol protocol = new TBinaryProtocol(tr);
        Cassandra.Client client = new Cassandra.Client(protocol);

        tr.open();
        if(!tr.isOpen()) {
            System.out.println("failed to connect server!");
            return;
        }

        String keyspace = "DEMO";  // 类似数据库名
        String columnFamily = "Users";   //类似数据表名
        String key = "1237";   //类似记录唯一ID或者主键
        String c1 = "name";
        String c2 = "password";

        client.set_keyspace(keyspace);  //设置数据库名

        ColumnParent columnParent = new ColumnParent(columnFamily); //设置数据表名

//        //创建name列
//        Column column1 = new Column(toByteBuffer(c1));
//        column1.setValue("yr".getBytes(CODE));
//        column1.setTimestamp(System.currentTimeMillis());
//
//        //创建password列
//        Column column2 = new Column(toByteBuffer(c2));
//        column2.setValue("fish".getBytes(CODE));
//        column2.setTimestamp(System.currentTimeMillis());
//
//        //插入创建的列
//        client.insert(toByteBuffer(key), columnParent, column1, ConsistencyLevel.ALL);
//        client.insert(toByteBuffer(key), columnParent, column2, ConsistencyLevel.ALL);

        /* 开始查询 */
        SlicePredicate predicate = new SlicePredicate();
        predicate.setSlice_range(new SliceRange(toByteBuffer(""), toByteBuffer(""), false, 100));
        List<ColumnOrSuperColumn> columns = client.get_slice(toByteBuffer(key), columnParent, predicate, ConsistencyLevel.ALL);
        System.out.println("获取一个key对应的多个column：");
        for(ColumnOrSuperColumn column : columns) {
            Column c = column.getColumn();
            System.out.println(toString(c.name) + " -> " + toString(c.value));
        }

        /* 查询单个列 */
        ColumnPath columnPath = new ColumnPath(columnFamily);
//        columnPath.setColumn(toByteBuffer(c1));
//        System.out.println("获取一个key对应的单个指定column：");
//        System.out.println(c1 + " -> " + toString(client.get(toByteBuffer(key), columnPath, ConsistencyLevel.ONE).column.value));

        /* 删除列或者记录 */
        client.remove(toByteBuffer(key), columnPath, System.currentTimeMillis(), ConsistencyLevel.ALL);

        /* 获取所有的Key */
        KeyRange keyRange = new KeyRange(100);
        keyRange.setStart_key(toByteBuffer(""));
        keyRange.setEnd_key(toByteBuffer(""));
        List<KeySlice> keys = client.get_range_slices(columnParent, predicate, keyRange, ConsistencyLevel.ONE);
        System.out.println("总共包含key的数目：" + keys.size() + "， 以下为key列表：");
        for (KeySlice ks : keys) {
            System.out.println(new String(ks.getKey()));
        }

        tr.close();
    }

    public static ByteBuffer toByteBuffer(String value) throws UnsupportedEncodingException {
        return ByteBuffer.wrap(value.getBytes(CODE));
    }

    public static String toString(ByteBuffer buffer) throws UnsupportedEncodingException {
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return new String(bytes, CODE);
    }
}
