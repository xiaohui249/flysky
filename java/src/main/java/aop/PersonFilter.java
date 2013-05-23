package aop;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-5-23
 * Time: 下午6:07
 * To change this template use File | Settings | File Templates.
 */
public class PersonFilter implements CallbackFilter {

    private static final int AUTH_NEED     = 0;
    private static final int AUTH_NOT_NEED = 1;

    /**
     * <pre>
     * 选择使用的proxy
     * 如果调用query函数，则使用第二个proxy
     * 否则，使用第一个proxy
     * </pre>
     */
    @Override
    public int accept(Method method) {
        if ("getAge".equals(method.getName())) {
            return AUTH_NOT_NEED;
        }
        return AUTH_NEED;
    }

}