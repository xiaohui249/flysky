package print;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: shijinkui
 * Date: 13-6-3
 * Time: 上午11:54
 * To change this template use File | Settings | File Templates.
 */
public class RedisFacotry {

    private static RedisFacotry instance = new RedisFacotry();

    private static JedisPoolConfig poolConfig;
    private static JedisPool jedisPool;

    private RedisFacotry() {
        Properties p = load();
        String host = p.getProperty("individual.grouppic.jedis.server");

        //初始化连接池配置
        poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(50);
        poolConfig.setMaxIdle(10);
        poolConfig.setMaxWaitMillis(1000);

       //初始化连接池
        jedisPool = new JedisPool(poolConfig, host, 6379);

    }

    private static Properties load() {
        Properties properties = new Properties();
        InputStream is = null;
        try {
            is = RedisFacotry.class.getClassLoader().getResourceAsStream("smc-api-resources.properties");
            properties.load(is);
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

    public static RedisFacotry getInstance() {
        return instance;
    }

    public Jedis getRedisClient() {
        return jedisPool.getResource();
    }

    public void returnReidsClient(Jedis jedis) {
        jedisPool.returnResource(jedis);
    }

    public void returnBrokenReidsClient(Jedis jedis) {
        jedisPool.returnBrokenResource(jedis);
    }

    public static void main(String... args) {
        RedisFacotry facotry = RedisFacotry.getInstance();
        for(int i=0; i<=51; i++) {
            Jedis jedis = facotry.getRedisClient();
            System.out.println(jedis);
            facotry.returnReidsClient(jedis);
        }
    }

}
