package function;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-8-28
 * Time: 下午5:14
 * To change this template use File | Settings | File Templates.
 */
public class FunctionTest {

    private static List<String> list = ImmutableList.of("a", "b", "c");

    private static Function<String, String> function = new Function<String, String>() {
        @Override
        public String apply(@Nullable String s) {
            System.out.println("handle " + s + " ...");
            return "func_" + s;
        }
    };

    public static List<String> transfer(List<String> list, Function<String, String> func) {
        List<String> result = new ArrayList<String>();
        Iterator<String> it = list.iterator();
        while(it.hasNext()) {
            result.add(func.apply(it.next()));
        }
        return result;
    }

    public static void main(String[] args) {

        System.out.println("转换前...");
        for(String s : list) {
            System.out.println(s);
        }

        list = transfer(list, function);

        System.out.println("转换后...");
        for(String s : list) {
            System.out.println(s);
        }

        //官方提供的转换方法
        Collection<String> list2 = Collections2.transform((Collection)list, function);
        for(String s : list2) {
            System.out.println(s);
        }
    }

}
