import org.tarantool.core.TarantoolConnection;
import org.tarantool.core.Tuple;
import org.tarantool.pool.SocketChannelPooledConnectionFactory;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-4-16
 * Time: 下午6:50
 * To change this template use File | Settings | File Templates.
 */
public class TarantoolQuery {

    static String DEFAULT_ENCODE = "utf-8";
    final static SocketChannelPooledConnectionFactory factory =
            new SocketChannelPooledConnectionFactory("10.1.156.188", 33013, 1, 5);

    public static void queryUser() {
        TarantoolConnection tc = factory.getConnection();

        Tuple tuple = new Tuple(2).setString(0,"user-"+32, DEFAULT_ENCODE).setBoolean(1,true);
        Tuple _tuple = tc.findOne(1,1,0,tuple);

        if(_tuple != null) {
            System.out.println("userId=" + _tuple.getInt(0) + "; userName="+_tuple.getString(1, DEFAULT_ENCODE) +
                    "; sex="+_tuple.getBoolean(2)+"; age="+_tuple.getInt(3));
        }
    }

    public static void main(String[] args) {
        queryUser();
    }
}
