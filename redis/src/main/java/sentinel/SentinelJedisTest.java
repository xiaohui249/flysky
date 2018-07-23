package sentinel;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * SentinelJedisTest
 *
 * @author xiaoh
 * @create 2018-07-23 12:05
 **/
public class SentinelJedisTest {
    public static void main(String[] args) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        String masterName = "cluster1";
        Set<String> sentinels = new HashSet<String>();
        sentinels.add("monitor3.cs.os.kkws.cn:26379");
        sentinels.add("monitor4.cs.os.kkws.cn:26379");
        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(masterName, sentinels, poolConfig);
        HostAndPort currentHostMaster = jedisSentinelPool.getCurrentHostMaster();
        System.out.println(currentHostMaster.getHost()+"--"+currentHostMaster.getPort());//获取主节点的信息
        Jedis resource = jedisSentinelPool.getResource();
        String value = resource.get("foo");
        if(null == value) {
            resource.set("foo", "bar");
            System.out.println("set foo = bar");//设置键foo对应的value值
        } else {
            System.out.println(value);//获得键foo对应的value值
        }
        resource.close();
    }
}
