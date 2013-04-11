package print;


import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-4-10
 * Time: 下午2:41
 * 用户类
 */
public class Client {
    private String str;
    private PrintService printService;

    public Client(String str) {
        this.str = str;
    }

    public void print() {
        printService.print(str);
    }

    @Inject
    public void setPrintService(@Named("two") PrintService printService) {
        this.printService = printService;
    }
}
