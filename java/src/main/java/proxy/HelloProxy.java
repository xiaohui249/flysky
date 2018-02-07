package proxy;

/**
 * Hello接口的静态代理类
 *
 * @author xiaoh
 * @create 2017-11-29 19:56
 **/
public class HelloProxy implements Hello {

    private Hello hello;

    public HelloProxy() {
        hello = new HelloImpl();
    }

    @Override
    public void say(String name) {
        before();
        hello.say(name);
        after();
    }

    private void before() {
        System.out.println("Before hello");
    }

    private void after() {
        System.out.println("After hello");
    }
}
