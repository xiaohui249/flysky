package print;

import com.google.inject.ImplementedBy;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-4-10
 * Time: 下午2:39
 * To change this template use File | Settings | File Templates.
 */
@ImplementedBy(PrintServiceImpl.class)
public interface PrintService {
    void print(String str);
}
