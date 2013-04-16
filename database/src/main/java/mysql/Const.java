package mysql;

import com.sohu.cachedb.tarantool.config.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-4-16
 * Time: 下午2:14
 * To change this template use File | Settings | File Templates.
 */
public class Const {
    public static Map<Integer, String> typeMap = new HashMap<Integer, String>();
    static {
        typeMap.put(-5, Constants.TYPE_LONG);
        typeMap.put(4, Constants.TYPE_INT);
        typeMap.put(12, Constants.TYPE_STR);
    }
}
