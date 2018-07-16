package share;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.Arrays;
import java.util.List;

/**
 * ShardedJedisTest
 *
 * @author xiaoh
 * @create 2018-06-22 14:46
 **/
public class ShardedJedisTest {
    private static final Logger logger = LoggerFactory.getLogger(ShardedJedisTest.class);

    public static void main(String[] args) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);
        poolConfig.setMaxWaitMillis(2000);
        poolConfig.setTestOnBorrow(true);

        JedisShardInfo shardInfo1 = new JedisShardInfo("monitor3.cs.os.kkws.cn", 6379, 500);
        JedisShardInfo shardInfo2 = new JedisShardInfo("monitor4.cs.os.kkws.cn", 6379, 500);
        List<JedisShardInfo> infoList = Arrays.asList(shardInfo1, shardInfo2);

        try(ShardedJedisPool shardedJedisPool = new ShardedJedisPool(poolConfig, infoList);
            ShardedJedis jedis = shardedJedisPool.getResource()) {

            long s = System.currentTimeMillis();
            String str;
            int max = 100;
            for(int i=0; i<max; i++) {
                str = "message" + i;
//                jedis.set(str, str);
                logger.info("get message: key = {}, value = {}", str, jedis.get(str));
            }
//            logger.info("set {} messages, cost {}ms", max, System.currentTimeMillis()-s);

//            jedis.set("cnblog", "cnblog");
//            jedis.set("redis", "redis");
//            jedis.set("test", "test");
//            jedis.set("123456", "1234567");
//            Client client1 = jedis.getShard("cnblog").getClient();
//            Client client2 = jedis.getShard("redis").getClient();
//            Client client3 = jedis.getShard("test").getClient();
//            Client client4 = jedis.getShard("123456").getClient();

            //打印key在哪个server中
//            logger.info("cnblog in server:" + client1.getHost() + " and port is:" + client1.getPort());
//            logger.info("redis  in server:" + client2.getHost() + " and port is:" + client2.getPort());
//            logger.info("test   in server:" + client3.getHost() + " and port is:" + client3.getPort());
//            logger.info("123456 in server:" + client4.getHost() + " and port is:" + client4.getPort());

//            System.out.println(jedis.get("cnblog"));
//            System.out.println(jedis.get("redis"));
//            System.out.println(jedis.get("test"));
//            System.out.println(jedis.get("123456"));
        }
    }
}
