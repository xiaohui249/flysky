package client;

import com.netflix.astyanax.ColumnListMutation;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.util.RangeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-8-16
 * Time: 下午1:53
 * To change this template use File | Settings | File Templates.
 */
public class AstyanaxClient {

    private final static Logger log = LoggerFactory.getLogger(AstyanaxClient.class);

    private Keyspace keyspace;

    public AstyanaxClient(Keyspace keyspace) {
        this.keyspace = keyspace;
    }

    /**
     * 构造ColumnFamily对象
     * @param columnFamily
     * @return
     */
    public ColumnFamily columnFamily(String columnFamily) {
        return new ColumnFamily(columnFamily, StringSerializer.get(), StringSerializer.get());
    }

    /**
     * 插入数据
     * @param columnFamily
     * @param key
     * @param columns
     */
    public void insert(String columnFamily, String key, Map<Long, Integer> columns) {
        ColumnFamily cf = columnFamily(columnFamily);
        insert(cf, key, columns);
    }

    /**
     * 多条数据插入
     * @param cf
     * @param key
     * @param columns
     */
    public void insert(ColumnFamily cf, String key, Map<Long, Integer> columns) {
        MutationBatch m = keyspace.prepareMutationBatch();
        ColumnListMutation mutation = m.withRow(cf, key);
        for(Map.Entry<Long, Integer> entry : columns.entrySet()) {
            mutation.putColumn(entry.getKey(), entry.getValue(), null);
        }

        try {
            OperationResult<Void> result = m.execute();
            log.info("Attemps Count : " + result.getAttemptsCount());
        } catch (ConnectionException e) {
            e.printStackTrace();
            log.error("multi insert failed!" + e.getMessage());
        }
    }

    /**
     * 单条数据插入
     * @param cf
     * @param key
     * @param name
     * @param value
     */
    public void insert(ColumnFamily cf, String key, Long name, Integer value) {
        MutationBatch m = keyspace.prepareMutationBatch();
        m.withRow(cf, key).putColumn(name, value);
        try {
            OperationResult<Void> result = m.execute();
            log.info("Attemps Count : " + result.getAttemptsCount());
        } catch (ConnectionException e) {
            e.printStackTrace();
            log.error("insert failed!" + e.getMessage());
        }
    }

    /**
     * 单个关键字查询
     * @param columnFamily
     * @param key
     * @return
     */
    public ColumnList<Long> get(String columnFamily, String key) {
        ColumnFamily cf = columnFamily(columnFamily);
        return get(cf, key);
    }

    /**
     * 单个关键字查询
     * @param cf
     * @param key
     * @return
     */
    public ColumnList<Long> get(ColumnFamily cf, String key) {
        OperationResult<ColumnList<Long>> result = null;
        try {
            result = keyspace.prepareQuery(cf)
                            .getKey(key)
                            .execute();
        }catch (ConnectionException e) {
            e.printStackTrace();
            log.error("get failed!" + e.getMessage());
        }
        return (result == null) ? null : result.getResult();
    }

    /**
     * 单个关键字查询，并可以对结果设置排序
     * @param cf
     * @param key
     * @param reverse
     * @return
     */
    public ColumnList<Long> get(ColumnFamily cf, String key, boolean reverse) {
        OperationResult<ColumnList<Long>> result = null;
        try {
            if(reverse) {
                result = keyspace.prepareQuery(cf)
                        .getKey(key)
                        .withColumnRange(new RangeBuilder().setReversed(true).build())
                        .execute();
            }else {
                result = keyspace.prepareQuery(cf)
                        .getKey(key)
                        .execute();
            }
        }catch (ConnectionException e) {
            e.printStackTrace();
            log.error("get failed!" + e.getMessage());
        }
        return (result == null) ? null : result.getResult();
    }

    /**
     * 多个关键字查询
     * @param columnFamily
     * @param keys
     * @return
     */
    public Rows<String, Long> multiGet(String columnFamily, List<String> keys) {
        ColumnFamily cf = columnFamily(columnFamily);
        return multiGet(cf, keys);
    }

    /**
     * 多个关键字查询
     * @param cf
     * @param keys
     * @return
     */
    public Rows<String, Long> multiGet(ColumnFamily cf, List<String> keys) {
        OperationResult<Rows<String, Long>> result = null;
        try {
            result = keyspace.prepareQuery(cf)
                    .getKeySlice(keys)
                    .execute();
        }catch (ConnectionException e) {
            e.printStackTrace();
            log.error("multi get failed!" + e.getMessage());
        }

        return (result == null) ? null : result.getResult();
    }
}
