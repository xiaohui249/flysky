package com.sean.flysky.netty.tcp.client;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * TCP Client
 *
 * @author xiaoh
 * @create 2018-04-16 15:10
 **/
public class Client {
    public static void main(String[] args){
        // 同服务端相同，只是这里使用的是NioClientSocketChannelFactory
        final ChannelFactory factory = new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool(),
                8);

        // ClientBootstrap用于帮助客户端启动
        ClientBootstrap bootstrap = new ClientBootstrap(factory);
        // 由于客户端不包含ServerSocketChannel，所以参数名不能带有child.前缀
        bootstrap.setOption("tcpNoDelay", true);
//      bootstrap.setOption("keepAlive", true);

        bootstrap.setPipelineFactory(new ChannelPipelineFactory(){
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline channelPipeline =
                        Channels.pipeline(new ClientReadDecoder(),
                                new ClientWriteEncoder(), new ClientLogicHandler());

                System.out.println(channelPipeline.hashCode());
                return channelPipeline;
            }
        });

        // 这里连接服务端绑定的IP和端口
        bootstrap.connect(new InetSocketAddress("10.7.3.191", 8000));
        System.out.println("Client is started...");
    }
}
