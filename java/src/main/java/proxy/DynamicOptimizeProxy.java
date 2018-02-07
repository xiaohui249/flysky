package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 优化后的JDK动态代理类
 *
 * @author xiaoh
 * @create 2017-11-29 21:13
 **/
public class DynamicOptimizeProxy implements InvocationHandler {
    private Object target;

    public DynamicOptimizeProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(target, args);
        after();
        return result;
    }

    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), this);
    }

    private void before() {
        System.out.println("Before hello optimized dynamic proxy");
    }

    private void after() {
        System.out.println("After hello optimized dynamic proxy");
    }
}
