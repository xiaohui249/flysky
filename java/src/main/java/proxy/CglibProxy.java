package proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Cglib动态代理类
 *
 * @author xiaoh
 * @create 2017-11-30 10:24
 **/
public class CglibProxy implements MethodInterceptor {
    private final static CglibProxy cglibProxy = new CglibProxy();

    private CglibProxy() {}

    public static CglibProxy getInstance() {
        return cglibProxy;
    }

    public <T> T getProxy(Class<T> cls) {
        return (T) Enhancer.create(cls, this);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        before();
        Object result = proxy.invokeSuper(o, args);
        after();
        return result;
    }

    private void before() {
        System.out.println("Before hello dynamic proxy with cglib");
    }

    private void after() {
        System.out.println("After hello dynamic proxy with cglib");
    }
}
