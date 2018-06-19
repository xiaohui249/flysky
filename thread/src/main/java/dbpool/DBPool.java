package dbpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-9
 * Time: 下午11:32
 * 连接池采用阻塞队列实现，结合阻塞存取和非阻塞存取方式，加上AtomaticInteger原子对象
 */
public class DBPool {
    private BlockingQueue<DBConnection> connections;
    private AtomicInteger check = new AtomicInteger(0); //正在使用连接数
    private int minPoolSize;
    private int maxPoolSize;

    private static DBPool instance;

    private DBPool(int min, int max) {
        minPoolSize = min;
        maxPoolSize = max;

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

    public DBConnection getConn() {
        System.out.println("**获取连接前**    可用连接数：" + connections.size() + ", 正在使用连接数：" + check.intValue());
        DBConnection conn = null;
        try{
            conn = connections.poll();
            if(conn == null || conn.isClosed()) {
                if(check.intValue() < maxPoolSize) {
                    System.out.println("生成新的连接对象......");
                    conn = new DBConnection();
                }else{
                    conn = connections.take();
                }
            }
            check.incrementAndGet();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("**获取连接后**    可用连接数：" + connections.size() + ", 正在使用连接数：" + check.intValue());
        return conn;
    }

    public void freeConn(DBConnection conn) {
        System.out.println("**释放连接前**    可用连接数：" + connections.size() + ", 正在使用连接数：" + check.intValue());
        try{
            boolean flag = connections.offer(conn);
            if(!flag){
                if(connections.size() == maxPoolSize) {
                    System.out.println("关闭连接......");
                    conn.close();
                }else{
                    connections.put(conn);
                }
            }
            check.decrementAndGet();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("**释放连接后**    可用连接数：" + connections.size() + ", 正在使用连接数：" + check.intValue());
    }
}
