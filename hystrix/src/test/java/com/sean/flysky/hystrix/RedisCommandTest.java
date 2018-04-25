package com.sean.flysky.hystrix;

import org.junit.Assert;
import org.junit.Test;

public class RedisCommandTest {
    @Test
    public void getFallback() throws Exception {
        RedisCommand command = new RedisCommand("sean");
//        Assert.assertEquals("sean", command.execute());
        Assert.assertEquals("sorry, I meet trouble!", command.execute());
    }

}