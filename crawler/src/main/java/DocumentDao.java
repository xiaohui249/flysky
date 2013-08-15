import model.DocumentObj;
import org.springframework.jdbc.core.JdbcTemplate;

import java.nio.charset.Charset;
import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-6-28
 * Time: 下午4:45
 * To change this template use File | Settings | File Templates.
 */
public class DocumentDao {
    private JdbcTemplate jdbcTemplate;

    public DocumentDao() {
        jdbcTemplate = BeanManager.getTemplate();
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
