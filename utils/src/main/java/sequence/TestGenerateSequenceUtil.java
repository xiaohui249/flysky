package sequence;

import sequence.GenerateSequenceUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-6-20
 * Time: 下午5:06
 * To change this template use File | Settings | File Templates.
 */
public class TestGenerateSequenceUtil {
    public static void main(String[] args) {
        Set<Long> set = new HashSet<Long>(1000);
        long s = System.currentTimeMillis();
        for(int i=0; i<1000000; i++) {
//            long id = Long.parseLong(GenerateSequenceUtil.generateSequenceNo());
            long id = GenerateSequenceUtil.getUniqueId();
            set.add(id);
            System.out.println(i + " : " + id);
        }
        System.out.println((System.currentTimeMillis() - s) + "ms");
        System.out.println("size : " + set.size());
    }
}
