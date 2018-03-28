package com.sean.flysky.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

import java.util.concurrent.TimeUnit;

/**
 * time out
 *
 * @author xiaoh
 * @create 2018-03-28 16:21
 **/
public class TimeOutCommand extends HystrixCommand<String> {
    private final String name;
    public TimeOutCommand(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("TimeoutGroup"))
            /* 配置依赖超时时间,500毫秒*/
            .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                    .withExecutionIsolationThreadTimeoutInMilliseconds(500)));
        this.name = name;
    }

    @Override
    protected String getFallback() {
        return "Execute failed!";
    }

    @Override
    protected String run() throws Exception {
        //sleep 1 秒,调用会超时
        TimeUnit.MILLISECONDS.sleep(1000);
        return "Hello " + name + "! thread: " + Thread.currentThread().getName();
    }

    public static void main(String[] args) {
        TimeOutCommand command = new TimeOutCommand("test-Fallback");
        String result = command.execute();
        System.out.println("result = " + result);
    }
}
