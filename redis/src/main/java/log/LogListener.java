package log;

import com.xiaoleilu.hutool.setting.Setting;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * 日志监听器
 *
 * @author xiaoh
 * @create 2018-01-11 17:35
 **/
public class LogListener {
    private static final Logger logger = LoggerFactory.getLogger(LogListener.class);
    private static final String DEFAULT_GROUPNAME = "default";

    public static void main(String[] args) {
        Setting setting = new Setting("app.properties");
        String queueName = setting.getString("queueName", DEFAULT_GROUPNAME);
        Jedis jedis = getJedis(setting);

        List<String> list;
        while(true) {
            list = jedis.brpop(3, queueName);
            if(list != null) {
                String message = list.get(1);
                logger.info(message);
            }
        }
    }

    private static Jedis getJedis(Setting setting) {
        String host = setting.get("host",DEFAULT_GROUPNAME);
        int port = setting.getInt("port", DEFAULT_GROUPNAME);
        String passwd = setting.getString("passwd", DEFAULT_GROUPNAME);
        String db = setting.get("db", DEFAULT_GROUPNAME);

        Jedis jedis = new Jedis(host, port);
        if(StringUtils.isNotBlank(passwd)) {
            jedis.auth(passwd);
        }
        if(db != null) {
            jedis.select(Integer.parseInt(db));
        }

        return jedis;
    }
}
