package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-8-22
 * Time: 下午4:01
 * 使用java.nio实现的简单Server
 */
public class NIOServer {

    private int flag = 0;
    private int BLOCK = 1024;
    private ByteBuffer sendBuffer = ByteBuffer.allocate(BLOCK);
    private ByteBuffer receiveBuffer = ByteBuffer.allocate(BLOCK);

    private Selector selector;

    //初始化Server
    public NIOServer(int port) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        ServerSocket socket = ssc.socket();
        socket.bind(new InetSocketAddress(port));

        selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT); //注册ACCEPT事件

        System.out.println("Server start on " + port + " port.");
    }

    //监听
    public void listen() throws IOException {
        while(true) {
            int num = selector.select();  //此处阻塞，如果有已注册类型（如上面的ACCEPT）到来，则唤醒继续
            if(num > 0) {
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                while(it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();
                    handleKey(key);
                }
            }
        }
    }

    //处理
    public void handleKey(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = null;
        SocketChannel sc = null;
        String receiveMessage;
        String sendMessage;
        if(key.isAcceptable()) {
            ssc = (ServerSocketChannel) key.channel();
            sc = ssc.accept();  //获取client的channel，统一使用该channel进行通信。
            sc.configureBlocking(false);
            //当服务端可以接受客户端请求之后，给客户端channel绑定可读事件，客户端将要发送给服务端的信息读到buffer中(内存中)。
            sc.register(selector, SelectionKey.OP_READ);
        }else if(key.isReadable()) {
            sc = (SocketChannel) key.channel();
            receiveBuffer.clear();
            int count = sc.read(receiveBuffer);
            if(count > 0) {
                receiveMessage = new String(receiveBuffer.array(),0,count);
                System.out.println("服务器端接受客户端数据--:" + receiveMessage);
                //
                sc.register(selector, SelectionKey.OP_WRITE);
            }
        }else if(key.isWritable()) {
            sc = (SocketChannel) key.channel();
            sendBuffer.clear();
            sendMessage = "message from server--" + flag++;
            sendBuffer.put(sendMessage.getBytes());
            sendBuffer.flip();
            sc.write(sendBuffer);
            System.out.println("服务器端向客户端发送数据--:" + sendMessage);
            sc.register(selector, SelectionKey.OP_READ);
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 8888;
        NIOServer server = new NIOServer(port);
        server.listen();
    }
}
