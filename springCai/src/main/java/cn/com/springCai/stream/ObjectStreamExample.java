package cn.com.springCai.stream;

import java.io.*;

/**
 * @Author caiJH
 * @Date 2025/6/11 8:23 PM
 * @Version 1.0
 * 对象流 I/O 需要实现Serializable标记接口
 * 用户对象持久化和网络传输 （实例化与反序列化）
 */
public class ObjectStreamExample implements Serializable {
    public static void main(String[] args) throws Exception {
        Person person = new Person("张三", 18);
//        Person person = new Person();
//        person.setName("张三");
//        person.setAge(18);
        // 序列化
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("/Users/caijinhui/PDF/person.txt"));
        oos.writeObject(person);
        // 手动将缓存中的数据发送到硬盘中进行持久化，而不需要等到缓存满了出发发送操作
//        oos.flush();
        oos.close();

        // 反序列化
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/Users/caijinhui/PDF/person.txt"));
        Person person1 = (Person) ois.readObject();
        System.out.println(person1.getName());
        System.out.println(person1.getAge());
        ois.close();

    }
}

/**
 * 对象流 (对象)
 * InvalidClassException 如果serialVersionUID不匹配
 * ObjectOutputStream ObjectInputStream java内置的对象序列化和反序列化依赖serialVersionUID
 * 用于确定对象是否一致，不一致则不可以进行反序列化
 */
class Person implements Serializable {
    private static final long serialVersionUID = 8661045159833305858L;
    private String name;
    private int age;

    public Person() {
    }

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

//    private void writeObject(ObjectOutputStream oos) throws IOException {
//        oos.defaultWriteObject();
//    }
//
//    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
//        ois.defaultReadObject();
//        // 处理新增字段的反序列化
//        if (true) {
//            this.age = 44;
//        }
//    }

    @Override
    public String toString() {
        return "Person:{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
