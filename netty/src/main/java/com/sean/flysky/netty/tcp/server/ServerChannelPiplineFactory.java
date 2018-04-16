package com.sean.flysky.netty.tcp.server;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import java.util.concurrent.Executor;

/**
 * ServerChannelPipelineFactory
 *
 * @author xiaoh
 * @create 2018-04-16 15:30
 **/
public class ServerChannelPiplineFactory implements ChannelPipelineFactory {
    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ServerReadDecoder serverReadDecoder = new ServerReadDecoder();
        ServerWriteEncoder serverWriteEncoder = new ServerWriteEncoder();

        Executor executor = new OrderedMemoryAwareThreadPoolExecutor(4, 200, 200);
        ServerExecutionHandler serverExecutionHandler = new ServerExecutionHandler(executor);
        ServerLogicHandler serverLogicHandler = new ServerLogicHandler();

        // ChannelPipeline的源码中的javadoc介绍的非常详细，很有必要看一下
        // ChannelPipeline是一个处理ChannelEvent的handler链
        // 如果为读操作，ChannelEvent事件会从前到后依次被
        // Upstream的handler处理
        // serverReadDecoder -> serverLogicHandler
        // 如果为写操作，ChannelEvent事件会从后至前依次被
        // Downstream的handler处理
        // serverLogicHandler -> serverWriteEncoder
        ChannelPipeline channelPipeline = Channels.pipeline();
        channelPipeline.addLast("1", serverReadDecoder);
        channelPipeline.addLast("2", serverWriteEncoder);
        channelPipeline.addLast("3", serverExecutionHandler);
        channelPipeline.addLast("4", serverLogicHandler);

        System.out.println(channelPipeline.hashCode());
        return channelPipeline;
    }
}
