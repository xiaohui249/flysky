package pushpop;

import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Redis Poper
 *
 * @author xiaoh
 * @create 2018-05-25 11:33
 **/
public class RedisPoper {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("10.7.3.191", 6379);
        List<String> list;
        int i = 0;
        while(true) {
            list = jedis.brpop(10, "myQueue");
            if(list != null) {
                String message = list.get(1);
                System.out.println(message);
                i++;
            } else {
                System.out.println("count = " + i);
            }
        }
    }
}
