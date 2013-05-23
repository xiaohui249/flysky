package aop;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-5-23
 * Time: 下午5:35
 * To change this template use File | Settings | File Templates.
 */
public class PersonFactory {
    private static Person person = new Person("sean", 27);

    public static Person getInstance() {
        return person;
    }

    public static Person getInstance(PersonProxy proxy) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Person.class);
        enhancer.setCallbacks(new Callback[] { proxy, NoOp.INSTANCE });
        enhancer.setCallbackFilter(new PersonFilter());
        return (Person) enhancer.create();
    }
}
