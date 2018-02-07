package com.sean.flysky.netty.tcp.server;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: sean
 * Date: 13-12-30
 * Time: 下午3:55
 * To change this template use File | Settings | File Templates.
 */
public class TcpServer {

    public static void main(String[] args) {
        ServerBootstrap server = new ServerBootstrap(new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(), Executors.newCachedThreadPool()
        ));

        server.setPipelineFactory(new TcpPipelineFactory(new ServerHandler()));

        server.setOption("reuseAddress", true);
        server.bind(new InetSocketAddress(8080));
        System.out.println("Tcp Server Listen Port: 8080");

    }

    static class ServerHandler extends SimpleChannelUpstreamHandler {
        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
                throws Exception {
            System.out.println("Channel closed " + e);
        }

        @Override
        public void channelConnected(ChannelHandlerContext ctx,
                                     ChannelStateEvent e) throws Exception {
            System.out.println("Channel connected " + e);
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
                throws Exception {
            try {
                System.out.println("New message " + e.toString() + " from "
                        + ctx.getChannel());
                processMessage(e);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }

        private void processMessage(MessageEvent e) {
            Channel ch = e.getChannel();
            ch.write(e.getMessage());
        }
    }

    static class TcpPipelineFactory implements ChannelPipelineFactory {

        private final ChannelHandler handler;

        public TcpPipelineFactory(ChannelHandler handler) {
            this.handler = handler;
        }

        @Override
        public ChannelPipeline getPipeline() throws Exception {
            ChannelPipeline pipeline = Channels.pipeline();

            pipeline.addLast("decoder", new StringDecoder());
            pipeline.addLast("encoder", new StringEncoder());

            return pipeline;
        }
    }

}
