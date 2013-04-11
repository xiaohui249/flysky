package print;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-4-10
 * Time: 下午2:40
 * To change this template use File | Settings | File Templates.
 */
public class PrintServiceImpl implements PrintService {
    @Override
    public void print(String str) {
        System.out.println(str+","+this);
    }
}
