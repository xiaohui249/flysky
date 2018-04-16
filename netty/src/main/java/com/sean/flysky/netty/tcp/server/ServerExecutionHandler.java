package com.sean.flysky.netty.tcp.server;

import org.jboss.netty.handler.execution.ExecutionHandler;

import java.util.concurrent.Executor;

/**
 * ServerExecutionHandler
 *
 * @author xiaoh
 * @create 2018-04-16 15:40
 **/
public class ServerExecutionHandler extends ExecutionHandler {
    public ServerExecutionHandler(Executor executor) {
        super(executor);
    }
}
