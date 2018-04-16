package com.sean.flysky.netty.tcp.client;

import org.jboss.netty.channel.*;

/**
 * ClientLogicHandler
 *
 * @author xiaoh
 * @create 2018-04-16 15:45
 **/
public class ClientLogicHandler extends SimpleChannelHandler {
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
        System.out.println("######channelConnected");

        Channel ch = e.getChannel();
        String msg = "Hi, Server.";
        ch.write(msg);
    }

    @Override
    public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e)
            throws Exception {
        System.out.println("######writeComplete");
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        System.out.println("######messageReceived");

        String msg = (String)e.getMessage();
        System.out.println("The message gotten from server is : " + msg);

        ChannelFuture channelFuture = e.getChannel().close();
        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();
        Channel ch = e.getChannel();
        ch.close();
    }
}
