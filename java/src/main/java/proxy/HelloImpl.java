package proxy;

/**
 * Hello接口实现类
 *
 * @author xiaoh
 * @create 2017-11-29 19:55
 **/
public class HelloImpl implements Hello {
    @Override
    public void say(String name) {
        System.out.println("Hello, " + name + "!");
    }
}
