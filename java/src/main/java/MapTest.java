import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-28
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */
public class MapTest {
    public static void main(String[] args) {
        Map<String, String> map = new TreeMap<String, String>();
        map.put(null, "x");
//        map.put(null, "x1");

        map = new HashMap<String, String>();
        map.put(null, "y");
        map.put(null, "y1");
        map.put("key", null);
        System.out.println(map.get(null));

        map = new Hashtable<String, String>();
        map.put(null, "z");
        map.put(null, "z1");
        System.out.println(map.get(null));
    }
}
