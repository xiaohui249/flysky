package print;

import com.google.inject.Guice;
import com.google.inject.Injector;
import junit.framework.TestCase;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-4-10
 * Time: 下午2:43
 * Guice使用测试
 */
public class TestClient extends TestCase {
    public void testPrint()
    {
        //创建注入模板
        PrintModule module = new PrintModule();

        //创建注入器
        Injector injector = Guice.createInjector(module);

        Client client = new Client("Hello, world!");

        //开始注入
        injector.injectMembers(client);

        client.print();
    }

}
