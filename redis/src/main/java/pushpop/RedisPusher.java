package pushpop;

import redis.clients.jedis.Jedis;

/**
 * Redis Pusher
 *
 * @author xiaoh
 * @create 2018-05-25 11:25
 **/
public class RedisPusher {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("10.7.3.191", 6379);
        for(int i=0; i<500000; i++) {
            jedis.lpush("myQueue", "message" + i);
        }
        jedis.close();
    }
}
