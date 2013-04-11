package dbpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-9
 * Time: 下午11:32
 * To change this template use File | Settings | File Templates.
 */
public class DBPool {
    private static BlockingQueue<DBConnection> connections;
    private static int checkOut;

    private static int minPoolSize;
    private static int maxPoolSize;

    private static DBPool instance;

    private final static Semaphore semp = new Semaphore(5);

    private DBPool(int min, int max) {
        minPoolSize = min;
        maxPoolSize = max;

        checkOut = 0;
        connections = new ArrayBlockingQueue<DBConnection>(maxPoolSize);
        for(int i=0; i<minPoolSize; i++) {
            connections.offer(new DBConnection());
        }
        System.out.println("初始化连接池完成！");
    }

    public static synchronized DBPool getInstance(int min, int max) {
        if(instance == null) {
            instance = new DBPool(min, max);
        }
        return instance;
    }

    public synchronized DBConnection getConn() {
        System.out.println("**获取连接前**    可用连接数：" + connections.size() + ", 正在使用连接数：" + checkOut);
        DBConnection conn = null;
        try{
            System.out.println("剩余信号量：" + semp.availablePermits());
            semp.acquire();
            conn = connections.poll();
            if(conn == null || conn.isClosed()) {
                System.out.println("生成新的连接对象......");
                conn = new DBConnection();
            }
            checkOut++;
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("**获取连接后**    可用连接数：" + connections.size() + ", 正在使用连接数：" + checkOut);
        return conn;
    }

    public synchronized void freeConn(DBConnection conn) {
        System.out.println("**释放连接前**    可用连接数：" + connections.size() + ", 正在使用连接数：" + checkOut);
//        try{
            boolean flag = connections.offer(conn);
            if(!flag){
                System.out.println("关闭连接......");
                conn.close();
            }
            checkOut--;
//            semp.release();
//        }catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        System.out.println("**释放连接后**    可用连接数：" + connections.size() + ", 正在使用连接数：" + checkOut);
    }
}
