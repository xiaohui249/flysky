package com.sean.flysky.netty.tcp.server;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;

/**
 * 编码
 *
 * @author xiaoh
 * @create 2018-04-16 15:35
 **/
public class ServerWriteEncoder extends StringEncoder {
    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        System.out.println("######ServerWriteEncoder");

        String str = (String)msg;
        // 通过ChannelBuffers工具，为指定编码的指定字符串分配缓存空间
        // 并将String转为ChannelBuffer
        ChannelBuffer channelBuffer =
                ChannelBuffers.copiedBuffer(str, CharsetUtil.UTF_8);
        return channelBuffer;
    }
}
