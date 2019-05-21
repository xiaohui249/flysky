import redis.clients.jedis.Jedis;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;

public class RedisTest {
	
	private final static String HOST = "192.168.20.2";
	private final static int PORT = 6379;
	
	private final static int num = 10000;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		
		Jedis jedis = new Jedis(HOST, PORT);


//		jedis.flushDB();

//        testSortSet(jedis);
		
//		String status = jedis.set("foo", "bar");
//		System.out.println("foo = bar is set into redis : " + status);
//		
//		String value = jedis.get("foo");
//		System.out.println("the value of the key 'foo' is : " + value);
//		
//		status = jedis.set("foo", "new _bar");
//		System.out.println("foo = new_bar is set into redis : " + status);
//		
//		value = jedis.get("foo");
//		System.out.println("the new value of the key 'foo' is : " + value);
//		
//		Set<String> keySet = jedis.keys("?x");
//		System.out.println("All keys in redis : ");
//		for(String k : keySet) {
//			System.out.print(k + " ");
//		}
//		System.out.println();
		
		//压力测试
//		long start = System.currentTimeMillis();
//		for(int i = 0; i < num; i++) {
//			jedis.set("key"+i, "value"+i);
//		}
//		System.out.println("Set " +num+" key-values cost time " + (System.currentTimeMillis()-start) + "ms.");

//		start = System.currentTimeMillis();
//		for(int i = 0; i < num; i++) {
//			jedis.get("key"+i);
//		}
//		System.out.println("Get " +num+" key-values cost time " + (System.currentTimeMillis()-start) + "ms.");

		testTokenCode(jedis);

	}

	/**
	 * 测试token编码问题
	 * @param jedis
	 */
	public static void testTokenCode(Jedis jedis) throws Exception {
		jedis.auth("123456");
		String key = "889854342FE659DF5B5B6F604E94754E";
//		String jsonStr = jedis.get(key);
//		System.out.println(jsonStr);
		byte[] byteValue = jedis.get(key.getBytes("GBK"));
		System.out.println(new String(byteValue, "GBK"));
	}

    public static void testSortSet(Jedis jedis) {
        try {
            Set<String> setValues = jedis.zrangeByScore("hot2_0_5701520653262262292",0,9, 0,10);
            System.out.println(setValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public static String gb2312ToUtf8(String str) {
		String urlEncode = "" ;
		try {
			urlEncode = URLEncoder.encode (str, "UTF-8" );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return urlEncode;
	}

	public static String getUTF8StringFromGBKString(String gbkStr) {
		try {
			return new String(getUTF8BytesFromGBKString(gbkStr), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new InternalError();
		}
	}

	public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
		int n = gbkStr.length();
		byte[] utfBytes = new byte[3 * n];
		int k = 0;
		for (int i = 0; i < n; i++) {
			int m = gbkStr.charAt(i);
			if (m < 128 && m >= 0) {
				utfBytes[k++] = (byte) m;
				continue;
			}
			utfBytes[k++] = (byte) (0xe0 | (m >> 12));
			utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
			utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
		}
		if (k < utfBytes.length) {
			byte[] tmp = new byte[k];
			System.arraycopy(utfBytes, 0, tmp, 0, k);
			return tmp;
		}
		return utfBytes;
	}
}
