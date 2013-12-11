package test;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-8
 * Time: 上午11:26
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) {
//        String str = new String("World");
//        char ch[] = {'H', 'e', 'l', 'l', 'o'};
//        change(str, ch);
//        System.out.print(str + " and ");
//        System.out.println(ch);
//
//        DefaultClass dc = new DefaultClass("a");
//        System.out.println(dc.name);
//        change(dc);
//        System.out.println(dc.name);

        int _i=1, _j=2;
        change1(_i, _j);
        System.out.println("i = " + _i + ", j = " + _j);

        DefaultClass a = new DefaultClass("x");
        DefaultClass b = new DefaultClass("y");
        change2(a, b);
        System.out.println("dc1.name = " + a.name + ", dc2.name = " + b.name);

    }

    public static void change(String str, char ch[]) {
        str = "Change";
        ch = new char[]{'c', 'e', 'l', 'l', 'o'};
    }

    public static void change(DefaultClass dc) {
//        dc = new DefaultClass("b");   //重新备份，不会影响参数的值，相当于值传递
//        dc.name = "b";  //直接改变参数值，引用传递
        //下面也是引用传递
        DefaultClass _dc = dc;
        _dc.name = "b";
    }

    public static void change1(int i, int j) {
        j = 0;
        i = j;
    }

    /**
     * 形参为复制的引用，即值传递，只是此处的值为引用值，即对象的内存地址。
     * @param dc1
     * @param dc2
     */
    public static void change2(DefaultClass dc1, DefaultClass dc2) {
        dc2.name = "dc2";
        dc1 = dc2;
    }
}
