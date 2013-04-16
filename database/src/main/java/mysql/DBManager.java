package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-4-16
 * Time: 上午11:01
 * To change this template use File | Settings | File Templates.
 */
public class DBManager {

    /* 获取数据库连接的函数*/
    public static Connection getConnection(String url, String username, String pass) {
        Connection con = null;  //创建用于连接数据库的Connection对象
        try {
            Class.forName("com.mysql.jdbc.Driver");// 加载Mysql数据驱动
            con = DriverManager.getConnection(url, username, pass);// 创建数据连接
        } catch (Exception e) {
            System.out.println("数据库连接失败" + e.getMessage());
        }
        return con; //返回所建立的数据库连接
    }

    /*关闭数据库连接*/
    public static void closeConn(Connection conn) {
        try{
            conn.close();
        }catch (Exception e) {
            System.out.println("数据库关闭失败" + e.getMessage());
        }
    }

}
