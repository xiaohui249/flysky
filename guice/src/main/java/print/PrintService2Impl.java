package print;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-4-10
 * Time: 下午3:58
 * To change this template use File | Settings | File Templates.
 */
public class PrintService2Impl implements PrintService {
    int i = 0;
    @Override
    public void print(String str) {
        System.out.println("This is the second implement: " + str + "," + i++);
    }
}
