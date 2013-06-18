import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-6-17
 * Time: 下午4:22
 * To change this template use File | Settings | File Templates.
 */
public class Query {
    private static Sql2o sql2o;

    static {
        sql2o = new Sql2o("jdbc:mysql://localhost:3306/test", "root", "root");
    }

    public static List<Test> getAll() {
        String sql = "select id, name, age from test";
        return sql2o.createQuery(sql).executeAndFetch(Test.class);
    }

    public static Test getById(int id) {
        String sql = "select id, name, age from test where id = :id";
        return sql2o.createQuery(sql).addParameter("id", id).executeAndFetchFirst(Test.class);
    }

    public static int insert(Test test) {
        String sql = "insert into test(name, age) values (:name, :age)";
        Connection connection = sql2o.createQuery(sql).addParameter("name", test.getName()).addParameter("age", test.getAge()).executeUpdate();
        return connection.getResult();
    }

    public static int update(Test test) {
        String sql = "update test set name = :name, age = :age where id = :id";
        Connection connection = sql2o.createQuery(sql).addParameter("name", test.getName()).addParameter("age", test.getAge()).addParameter("id", test.getId()).executeUpdate();
        return connection.getResult();
    }

    public static void main(String[] args) {
        //查询所有
        List<Test> list = getAll();
        System.out.println("size = " + list.size());

        //按条件查询
        Test test = getById(3);
        System.out.println(test.getName());

        //插入测试
//        Test test1 = new Test();
//        test1.setName("yyy");
//        test1.setAge(25);
//        int result = insert(test1);
//        System.out.println("insert result: " + result);

        test.setAge(22);
        int result = update(test);
        System.out.println("update result: " + result);
    }
}
