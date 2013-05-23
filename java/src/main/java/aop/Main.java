package aop;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-5-23
 * Time: 下午5:36
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Person person = PersonFactory.getInstance();
        System.out.println(person.getName());
        System.out.println(person.getAge());

        testProxy("sss");
    }

    public static void testProxy(String name) {
        PersonProxy proxy = new PersonProxy(name);
        Person person = PersonFactory.getInstance(proxy);
        System.out.println(person.getName());
        System.out.println(person.getAge());
    }
}
