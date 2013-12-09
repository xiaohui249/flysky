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
        System.out.println(DefaultClass.class);

        String str = new String("World");
        char ch[] = {'H', 'e', 'l', 'l', 'o'};
        change(str, ch);
        System.out.print(str + " and ");
        System.out.println(ch);

        DefaultClass dc = new DefaultClass("a");
        System.out.println(dc.name);
        change(dc);
        System.out.println(dc.name);
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
}
