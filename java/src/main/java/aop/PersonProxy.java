package aop;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-5-23
 * Time: 下午5:40
 * 用户校验代理类
 */
public class PersonProxy implements MethodInterceptor {

    private String name;

    public PersonProxy(String name) {
        this.name = name;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if(!"sean".equals(this.name)) {
            System.out.println("PersonProxy:you have no permits to do manager!");
            return null;
        }
        return methodProxy.invoke(o, objects);
    }
}
