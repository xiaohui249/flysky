package client;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: sean
 * Date: 13-12-30
 * Time: 下午4:08
 * To change this template use File | Settings | File Templates.
 */
public class TcpClient {

    public static void main(String[] args) {

        ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(), Executors.newCachedThreadPool()
        ));

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new ClientHandler());
            }
        });

        bootstrap.setOption("remoteAddress", new InetSocketAddress("127.0.0.1", 8080));
        bootstrap.connect();

    }

    static class ClientHandler extends SimpleChannelUpstreamHandler {
        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
                throws Exception {
            System.out.println("Client Channel closed " + e);
        }

        @Override
        public void channelConnected(ChannelHandlerContext ctx,
                                     ChannelStateEvent e) throws Exception {
            System.out.println("Client Channel connected " + e);
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
                throws Exception {
            try {
                ChannelBuffer buf = (ChannelBuffer) e.getMessage();
                byte[] bytes = buf.array();
                System.out.println("Client reseived message : " + new String(bytes));

                Channel ch = e.getChannel();
                ChannelBuffer cb = ChannelBuffers.wrappedBuffer("1".getBytes()) ;
                ch.write(cb);

            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
    }

}
