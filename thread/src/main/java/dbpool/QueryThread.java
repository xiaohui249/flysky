package dbpool;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-9
 * Time: 下午11:57
 * To change this template use File | Settings | File Templates.
 */
public class QueryThread implements Runnable {
    static DBPool pool = DBPool.getInstance(2,10);
    private String name;

    public QueryThread(String name) {
        this.name = name;
    }
    @Override
    public void run() {
        System.out.println(name + "开始啦！准备获取连接...");
        DBConnection conn = pool.getConn();
        conn.execute();
        System.out.println(name + "执行结束了！");
        pool.freeConn(conn);
        System.out.println(name + "释放连接啦 ！");
    }
}
