package dbpool;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-9
 * Time: 下午11:32
 * To change this template use File | Settings | File Templates.
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
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
