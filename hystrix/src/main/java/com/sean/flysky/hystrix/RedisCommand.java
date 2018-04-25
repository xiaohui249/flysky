package com.sean.flysky.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import redis.clients.jedis.Jedis;

/**
 * Redis操作封装
 *
 * @author xiaoh
 * @create 2018-04-25 17:42
 **/
public class RedisCommand extends HystrixCommand<String> {

    private String name;

    public RedisCommand(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("redisCommand"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("redisCommandPool")));
        this.name = name;
    }

    @Override
    protected String getFallback() {
        return "sorry, I meet trouble!";
    }

    @Override
    protected String run() throws Exception {
        Jedis jedis = new Jedis("127.0.0.1", 6379);

        String result = jedis.get("a");
        if(null == result) {
            String result1 = jedis.set("a", name);
            System.out.println("set result = " + result1);
        }

        result = jedis.get("a");
        System.out.println("get result = " + result);

        return result;

//        throw new HystrixBadRequestException("a exception");
    }
}
