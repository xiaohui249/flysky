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
            InvalidRequestException, TException, UnavailableException, TimedOutException {
        TTransport tr = new TFramedTransport(new TSocket("10.13.80.151", 9160));
        TProtocol protocol = new TBinaryProtocol(tr);
        Cassandra.Client client = new Cassandra.Client(protocol);

        tr.open();
        System.out.println("Socket is open ? " + tr.isOpen());


        String keyspace = "DEMO";  // 类似数据库名
        String columnFamily = "Users";   //类似数据表名
        String key = "1236";   //类似记录唯一ID或者主键
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
        predicate.setSlice_range(new SliceRange(ByteBuffer.wrap(new byte[0]), ByteBuffer.wrap(new byte[0]), false, 100));
        List<ColumnOrSuperColumn> columns = client.get_slice(toByteBuffer(key), columnParent, predicate, ConsistencyLevel.ALL);
        for(ColumnOrSuperColumn column : columns) {
            Column c = column.getColumn();
            System.out.println(toString(c.name) + " -> " + toString(c.value));
        }

        /* 获取所有的Key */
        KeyRange keyRange = new KeyRange(100);
        keyRange.setStart_key(ByteBuffer.wrap(new byte[0]));
        keyRange.setEnd_key(ByteBuffer.wrap(new byte[0]));
        List<KeySlice> keys = client.get_range_slices(columnParent, predicate, keyRange, ConsistencyLevel.ONE);
        System.out.println(keys.size());
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
