package mysql;

import com.sohu.cachedb.tarantool.config.Constants;
import com.sohu.cachedb.tarantool.meta.MetaData;
import org.tarantool.core.TarantoolConnection;
import org.tarantool.core.Tuple;
import org.tarantool.facade.TarantoolTemplate;
import org.tarantool.pool.SocketChannelPooledConnectionFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-4-16
 * Time: 下午2:03
 * To change this template use File | Settings | File Templates.
 */
public class WriteIntoCacheDB {

    final static SocketChannelPooledConnectionFactory factory =
            new SocketChannelPooledConnectionFactory("10.1.156.188", 33013, 1, 5);

    public static void write(File file) {
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            for(File f : files) {
                write(f);
            }
        }else{
            System.out.println("FileName: " + file.getName());
            BufferedReader br = null;
            try{
                br = new BufferedReader(new FileReader(file));
                String temp  = br.readLine();
                int i = 1;
                MetaData metaData = null;
                while(temp != null) {
                    System.out.println(temp);
                    if(i == 1) {
                        metaData = writeMetaData(temp);
                    }else{
                        writeData(temp, metaData);
                    }
                    i++;
                    temp = br.readLine();
                }
            }catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static MetaData writeMetaData(String meta) {
        String[] metas = meta.split("\\$");
        if(metas.length < 3) {
            System.out.println(metas[0] = "元数据错误！");
            System.exit(0);
        }

        MetaData metaData = new MetaData();
        metaData.setTablename(metas[0]);
        metaData.setSpace(Integer.parseInt(metas[1]));
        metaData.setFields(metas[2]);
        if(metas.length > 3) {
            metaData.setIndexes(metas[3]);
        }

        TarantoolTemplate template = new TarantoolTemplate(factory);
        template.save(metaData).insertOrReplace();

        return metaData;
    }

    public static void writeData(String data, MetaData meta) {
        String[] ds = data.split("\\$");
        String[] fieldInfo = meta.getFields().split(";");
        if(ds.length != fieldInfo.length) {
            System.out.println("插入数据信息错误！");
            System.exit(0);
        }

        Tuple tuple = new Tuple(fieldInfo.length);
        for(int i=0; i<fieldInfo.length; i++) {
            setValue(tuple, fieldInfo[i].split("\\:")[1], ds[i]);
        }

        TarantoolConnection tc = factory.getConnection();
        int rs = tc.insertOrReplace(meta.getSpace(), tuple);
        if(rs == 0) {
            System.out.println(data + "插入" + meta.getTablename() + "失败！");
        }
    }

    private static void setValue(Tuple tuple, String fieldInfo, Object value) {
        if (value == null) {
            return;
        }
        String info[] = fieldInfo.split("\\|");
        String val = String.valueOf(value);
        if (info[1].equals(Constants.TYPE_INT)) {
            tuple.setInt(Integer.parseInt(info[0]), Integer.valueOf(val));
        } else if (info[1].equals(Constants.TYPE_STR)) {
            tuple.setString(Integer.parseInt(info[0]), val,
                    Constants.DEFAULT_ENCODE);
        } else if (info[1].equals(Constants.TYPE_BOOL)) {
            tuple.setBoolean(Integer.parseInt(info[0]), Boolean.valueOf(val));
        } else if(info[1].equals(Constants.TYPE_SHORT)) {
            tuple.setShort(Integer.parseInt(info[0]), Short.valueOf(val));
        } else if(info[1].equals(Constants.TYPE_LONG)) {
            tuple.setLong(Integer.parseInt(info[0]), Long.valueOf(val));
        }
    }

    public static void main(String[] args) {
        write(new File(Const.DATA_DIR));
    }

}
