package cn.com.springCai.functionalInterface;

import cn.com.springCai.service.entity.student.po.Student;

/**
 * @Author caiJH
 * @Date 2025/3/21 9:28 AM
 * @Version 1.0
 */
public class FunInterTest {

    public static void main(String[] args) {
        FunInterTest test = new FunInterTest();
        test.test();
    }

    /**
     * 函数式接口可以将lambda表达式作为实参传入方法中
     * 在调用函数式接口的方法时，才会执行具体的lambda表达式内容
     */
    public void test(){
        lambdaInterfaceTest("蔡进辉",()->{
            System.out.printf("进入函数式接口调用中\n");
            return getStudent();
        });
    }

    public Student getStudent(){
        Student student = new Student();
        student.setName("caiJinHui");
        student.setAddress("北京市，朝阳区");
        return student;
    }

    public void lambdaInterfaceTest(String name, MyFunctionalInterface<Student> functionalInterface){
        System.out.printf("str变量名:%s\n",name);
        try {
            //当调用函数式接口的方法是，执行test()函数中的lambda表达式
            Student object = functionalInterface.getObject();
            System.out.println(object.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
