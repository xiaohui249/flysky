package share;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.util.Hashing;

import java.util.*;

/**
 * ShardedJedisSentinelPool
 *
 * @author xiaoh
 * @create 2018-07-03 10:32
 **/
public class ShardedJedisSentinelPool {
    private Map<String,JedisSentinelPool> poolMap = new HashMap<String,JedisSentinelPool>();
    private Hashing algo = Hashing.MURMUR_HASH;
    private TreeMap<Long, JedisShardInfo> nodes;

    public ShardedJedisSentinelPool(Set<HostAndPort> hostInfos, Set<String> sentinels) {
        List<JedisShardInfo> list = new ArrayList<JedisShardInfo>();
        for(HostAndPort hostInfo : hostInfos) {
            JedisShardInfo info = new JedisShardInfo(hostInfo.getHost(),hostInfo.getPort());
            list.add(info);
        }

        initialize(list);

        for(HostAndPort iter : hostInfos){
            String masterName = "master-"+iter.getHost()+":"+iter.getPort();
            JedisSentinelPool sentinuelPool = new JedisSentinelPool(masterName, sentinels);
            poolMap.put(masterName, sentinuelPool);
        }
    }

    private void initialize(List<JedisShardInfo> shards) {
        nodes = new TreeMap<Long, JedisShardInfo>();

        for (int i = 0; i != shards.size(); ++i) {
            final JedisShardInfo shardInfo = shards.get(i);
            if (shardInfo.getName() == null) for (int n = 0; n < 160 * shardInfo.getWeight(); n++) {
                nodes.put(this.algo.hash("SHARD-" + i + "-NODE-" + n), shardInfo);
            }
            else for (int n = 0; n < 160 * shardInfo.getWeight(); n++) {
                nodes.put(this.algo.hash(shardInfo.getName() + "*" + shardInfo.getWeight() + n), shardInfo);
            }
        }
    }

    public Jedis getResource(String key){
        JedisShardInfo shardInfo = getShardInfo(key.getBytes());
        String masterName = "master-"+shardInfo.getHost()+":"+shardInfo.getPort();
        JedisSentinelPool sentinelPool = poolMap.get(masterName);
        Jedis sentinelJedis = sentinelPool.getResource();
        return sentinelJedis;
    }

    public JedisShardInfo getShardInfo(byte[] key) {
        SortedMap<Long, JedisShardInfo> tail = nodes.tailMap(algo.hash(key));
        if (tail.isEmpty()) {
            return nodes.get(nodes.firstKey());
        }
        return tail.get(tail.firstKey());
    }

    public void close() {
        for (Map.Entry<String, JedisSentinelPool> iter : poolMap.entrySet()) {
            iter.getValue().destroy();
        }
    }
}
