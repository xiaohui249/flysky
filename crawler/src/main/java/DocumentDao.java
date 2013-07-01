import model.DocumentObj;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-6-28
 * Time: 下午4:45
 * To change this template use File | Settings | File Templates.
 */
public class DocumentDao {
    private Sql2o sql2o;
    private JdbcTemplate jdbcTemplate;

    public DocumentDao() {
        sql2o = Executor.getSql2o();
        jdbcTemplate = BeanManager.getTemplate();
    }

    public int insert(DocumentObj dobj) {
        String sql = "insert into document(id, title, descr, url) values (:id, :title, :descr, :url)";
        int result = 0;
        try {
            Connection connection = sql2o.createQuery(sql).addParameter("id", dobj.getId()).addParameter("title", dobj.getTitle())
                    .addParameter("descr", dobj.getDesc()).addParameter("url", dobj.getUrl()).executeUpdate();
             result = connection.getResult();
        }catch (Sql2oException e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }

    public int insertByTemplate(DocumentObj obj) {
        String sql = "insert into document(id, title, descr, url) values (?, ?, ?, ?)";
        int result = 0;
        try{
            result = jdbcTemplate.update(sql, new Object[]{obj.getId(), obj.getTitle(), obj.getDesc(), obj.getUrl()});
        }catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }
}
