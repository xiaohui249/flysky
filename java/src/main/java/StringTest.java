/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-27
 * Time: 下午3:06
 * To change this template use File | Settings | File Templates.
 */
public class StringTest {
    public static void main(String[] args) {
        String s = "x";
        String s1 = new String("x1");
        change(s);
        change(s1);
        System.out.println(s + " : " + s1);

        Integer i = new Integer(5);
        Integer i1 = new Integer(135);
        change1(i);
        change1(i1);
        System.out.println(i + " : " + i1);

    }

    public static void change(String str) {
        str = str + "S";
    }

    public static void change1(Integer i) {
        i++;
    }
}
