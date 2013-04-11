package dbpool;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-9
 * Time: 下午11:32
 * 简单定义的连接对象
 */
public class DBConnection {
    private boolean isClosed;

    public DBConnection() {
        this.isClosed = false;
    }

    public boolean isClosed(){
        return isClosed;
    }

    public void close() {
        this.isClosed = true;
    }

    public void execute() {
        try{
            System.out.println("executing...");
            Thread.sleep(100*new Random().nextInt(10));
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
