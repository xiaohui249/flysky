package nio.mina;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-8-23
 * Time: 上午11:06
 * To change this template use File | Settings | File Templates.
 */
public class MinaClient {

    private final static long TIMEOUT = 1000;

    public static void main(String[] args) throws Exception {
        NioSocketConnector connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(TIMEOUT);

        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));
        connector.getFilterChain().addLast("logger", new LoggingFilter());

        connector.setHandler(new ClientSessionHandler());

        IoSession session;

        for (;;) {
            try {
                ConnectFuture future = connector.connect(new InetSocketAddress("localhost", 9123));
                future.awaitUninterruptibly();
                session = future.getSession();
                break;
            } catch (RuntimeIoException e) {
                System.err.println("Failed to connect.");
                e.printStackTrace();
                Thread.sleep(5000);
            }
        }

        // wait until the summation is done
        session.getCloseFuture().awaitUninterruptibly();
        connector.dispose();
    }

    static class ClientSessionHandler extends IoHandlerAdapter {
        @Override
        public void exceptionCaught( IoSession session, Throwable cause ) throws Exception
        {
            cause.printStackTrace();
        }

        @Override
        public void sessionOpened(IoSession session) {
            System.out.println("已连接");
            session.write("com.sean.flysky.netty.tcp.client message");
        }

        @Override
        public void messageReceived(IoSession session, Object message) {
            System.out.println("接收信息：" + message);
        }
    }
}
