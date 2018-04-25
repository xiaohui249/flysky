package com.sean.flysky.netty.tcp.server;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * TCP Server
 *
 * @author xiaoh
 * @create 2018-04-16 15:10
 **/
public class Server {
    private ChannelFactory channelFactory;

    public static ChannelGroup channelGroup = new DefaultChannelGroup();

    public void start() {
        /**
         NioServerSocketChannelFactory用于创建基于NIO的服务端ServerSocketChannel。
         本身包含2种线程，boss线程和worker线程。
         每个ServerSocketChannel会都会拥有自己的boss线程，当一个连接被服务端接受（accepted），
         boss线程就会将接收到的Channel传递给一个worker线程处理，
         而worker线程以非阻塞的方式为一个或多个Channel提供非阻塞的读写。
         */
        channelFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), //boss线程池
                                                            Executors.newCachedThreadPool(),  //worker线程池
                                                            8); //worker线程数
        // ServerBootstrap用于帮助服务器启动
        ServerBootstrap bootstrap = new ServerBootstrap(channelFactory);

        // 没有child.前缀，则该选项是为ServerSocketChannel设置
        bootstrap.setOption("reuseAddress", true);
        // 有child.前缀，则该选项是为Channel设置
//      bootstrap.setOption("child.tcpNoDelay", true);
//      bootstrap.setOption("child.keepAlive", true);

        ServerChannelPiplineFactory channelPiplineFactory = new ServerChannelPiplineFactory();
        bootstrap.setPipelineFactory(channelPiplineFactory);

        // 这里绑定服务端监听的IP和端口
        Channel channel = bootstrap.bind(new InetSocketAddress("10.7.3.191", 8000));
        Server.channelGroup.add(channel);

        System.out.println("Server is started...");
    }

    public void stop() {
        // ChannelGroup为其管理的Channels提供一系列的批量操作
        // 关闭的Channel会自动从ChannelGroup中移除
        ChannelGroupFuture channelGroupFuture = Server.channelGroup.close();
        channelGroupFuture.awaitUninterruptibly();
        channelFactory.releaseExternalResources();
        System.out.println("Server is stopped.");
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.start();
        Thread.sleep(120*1000);
        server.stop();
    }
}