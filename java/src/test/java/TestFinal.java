/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-10-17
 * Time: 上午10:00
 * To change this template use File | Settings | File Templates.
 */
public class TestFinal {
    public static void main(String[] args) {
        int i = 0;
        String z = "z";
        String x = new String("x");
        //final修饰变量，无法重新赋值
        i = 3;
        z = "z1";
        x = new String("y");
        System.out.println(i + " : " + z + " : " + x);

//        String[] arr = {"x", "y"};
//        //final修饰的对象变量，无法修改引用，即无法重新赋值；但是对象本身的值可以改变，如数组元素的值可以改变。
//        arr[1] = "z";
//        System.out.println(arr[0] + " : " + arr[1]);
//
//        String[] arr1 = {"a"};
//        System.arraycopy(arr1, 0, arr, 1, 1);
//        for(int j=0; j<arr.length; j++) {
//            System.out.println(arr[j] + " ");
//        }

        String abc = new String("abc");
        String _abc = "abc";
        String abc_ = new String("abc");

        //abc指向堆内存中的字符串对象，_abc指向常量池中的字符串常量；abc和_abc均为变量引用。
        System.out.println(abc == _abc);

        //以下语句推出：所有字面值相等的String对象，共享一个字符串常量，其位于字符串常量池
        System.out.println(abc.intern() == _abc);
        System.out.println(abc.intern() == abc_.intern());

    }
}
