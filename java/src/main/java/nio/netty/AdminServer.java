package nio.netty;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-8-23
 * Time: 下午4:08
 * 使用Netty实现的HTTP服务
 */
public class AdminServer {
    public static void main(String[] args) {
        start(8888);
    }

    public static void start(int port) {
        NioServerSocketChannelFactory factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        ServerBootstrap bootstrap = new ServerBootstrap(factory);

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder", new HttpRequestDecoder());
                pipeline.addLast("encoder",new HttpResponseEncoder());
                pipeline.addLast("handler", new AdminServerHandler("Server Handler"));
                return pipeline;
            }
        });
        bootstrap.setOption("tcpNoDelay" , true);
        bootstrap.setOption("keepAlive", true);
        bootstrap.bind(new InetSocketAddress(port));
        System.out.println("admin server start on "+port);

    }

    private static class AdminServerHandler extends SimpleChannelUpstreamHandler {

        private String name;

        public AdminServerHandler(String name) {
            this.name = name;
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

            HttpRequest request = (HttpRequest) e.getMessage();

            String uri = request.getUri();
            System.out.println(name + ": uri = " + uri);

            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

            ChannelBuffer buffer = new DynamicChannelBuffer(2048);
            buffer.writeBytes((name + " gives you hello! 你好！").getBytes("UTF-8"));

            response.setContent(buffer);
            response.setHeader("Content-Type", "text/html;charset=UTF-8");
            response.setHeader("Content-Length", response.getContent().writerIndex());

            Channel ch = e.getChannel();
            ch.write(response);
            ch.disconnect();
            ch.close();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
                throws Exception {
            Channel ch = e.getChannel();
            Throwable cause = e.getCause();
            if (cause instanceof TooLongFrameException) {
                sendError(ctx, HttpResponseStatus.BAD_REQUEST);
                return;
            }

            cause.printStackTrace();
            if (ch.isConnected()) {
                sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            }
        }

        private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
            response.setHeader("Content-Type", "text/plain; charset=UTF-8");
            response.setContent(ChannelBuffers.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));

            // Close the connection as soon as the error message is sent.
            ctx.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);
        }
    }


}
