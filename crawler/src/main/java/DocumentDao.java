import model.DocumentObj;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-6-28
 * Time: 下午4:45
 * To change this template use File | Settings | File Templates.
 */
public class DocumentDao {
    private final static Sql2o sql2o = Executor.getSql2o();

    public static int insert(DocumentObj dobj) {
//        String sql = "insert into document(id, title, desc, url) values (:id, :title, :desc, :url)";
//        Connection connection = sql2o.createQuery(sql).addParameter("id", dobj.getId()).addParameter("title", dobj.getTitle())
//                .addParameter("desc", dobj.getDesc()).addParameter("url", dobj.getUrl()).executeUpdate();

        String sql = "insert into document(id, title, desc, url) values ("+dobj.getId()+", '"+dobj.getTitle()+"', '"+dobj.getDesc()+"', '"+dobj.getUrl()+"')";
        sql = "insert into document(id, title, desc, url) values (1,, '测试', '测试', '测试')";
//        sql = Parser.encodeConvertor(sql, Charset.defaultCharset().name(), "UTF-8");
        Connection connection = sql2o.createQuery(sql).executeUpdate();
        return connection.getResult();
    }
}
