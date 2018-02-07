import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-27
 * Time: 下午8:31
 * To change this template use File | Settings | File Templates.
 */
public class ListTest {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList();
        list.add(1);
        list.add(2);
        for(Integer s : list) {
            System.out.println(s);
        }

        Set<String> set = new LinkedHashSet();
        set.add("b");
        set.add("a");
        set.add("c");
        for(String s: set) {
            System.out.println(s);
        }
    }
}
