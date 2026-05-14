package cn.com.springCai.test.applicationTest.bean;

import cn.com.springCai.test.applicationTest.bean.father.BaseService;
import org.springframework.stereotype.Service;

/**
 * @Author caiJH
 * @Date 2025/3/27 2:14 PM
 * @Version 1.0
 */
@Service("testService")
public class TestService extends BaseService {
    private String name = "cai";

    private char change;

    private int age;

    public TestService(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public TestService() {
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

    public char getChange() {
        return change;
    }

    public void setChange(char change) {
        this.change = change;
    }

    @Override
    public String toString() {
        return "TestService{" +
                "name='" + name + '\'' +
                ", change=" + change +
                ", age=" + age +
                '}';
    }
}
