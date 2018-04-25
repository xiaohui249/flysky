package com.sean.flysky.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * CommandHelloFailure
 *
 * @author xiaoh
 * @create 2018-04-25 14:13
 **/
public class CommandHelloFailure extends HystrixCommand<String> {
    private String name;
    protected CommandHelloFailure(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        throw new RuntimeException("this command always fails");
    }

    @Override
    protected String getFallback() {
        return "Hello Failure " + name + "!";
    }
}
