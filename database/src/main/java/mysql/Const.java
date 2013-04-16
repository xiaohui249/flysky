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
        typeMap.put(-7, Constants.TYPE_SHORT);  //  -7 : TINYINT
        typeMap.put(-6, Constants.TYPE_SHORT);  //  -6 : TINYINT
        typeMap.put(-5, Constants.TYPE_LONG);   //  -5 : BIGINT
        typeMap.put(4, Constants.TYPE_INT);     //  4 : INT
        typeMap.put(12, Constants.TYPE_STR);    //  12 : VARCHAR
        typeMap.put(93, Constants.TYPE_STR);   //  93 : DATETIME
    }

    public static String DATA_DIR = "F:/temp/data/";
    public static String CONF_DIR = "F:/temp/";

    public static String separator = System.getProperty("line.separator", "/n");

    public static Map<String, String> indexType = new HashMap<String, String>();
    static {
        indexType.put(Constants.TYPE_LONG, "NUM");
        indexType.put(Constants.TYPE_SHORT, "NUM");
        indexType.put(Constants.TYPE_INT, "NUM");
        indexType.put(Constants.TYPE_STR, "STR");
        indexType.put(Constants.TYPE_BOOL, "STR");
    }
}
