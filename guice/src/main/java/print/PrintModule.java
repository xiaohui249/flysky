package print;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-4-10
 * Time: 下午3:05
 * 打印服务的注入模板
 */
public class PrintModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(PrintService.class).annotatedWith(Names.named("one")).to(PrintServiceImpl.class);
        binder.bind(PrintService.class).annotatedWith(Names.named("two")).to(PrintService2Impl.class);
//        binder.bind(Key.get(PrintService.class)).to(PrintService2Impl.class).in(Scopes.SINGLETON);
    }
}
