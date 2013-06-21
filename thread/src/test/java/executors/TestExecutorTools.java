package executors;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-6-21
 * Time: 下午6:57
 * To change this template use File | Settings | File Templates.
 */
public class TestExecutorTools {
    public static void main(String[] args) {
        ExecutorTools.execute(new MyThread(), 5);
    }
}
