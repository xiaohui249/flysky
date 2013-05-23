package aop;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-5-23
 * Time: 下午5:33
 * To change this template use File | Settings | File Templates.
 */
public class Person {
    private String name;
    private int age;

    public Person() {}

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
