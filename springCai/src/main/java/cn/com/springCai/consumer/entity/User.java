package cn.com.springCai.consumer.entity;

import java.io.Serializable;

/**
 * @Author caiJH
 * @Date 2025/6/27 3:28 PM
 * @Version 1.0
 */
public class User implements Serializable {


    private static final long serialVersionUID = -2274273189292136227L;

    public User() {
    }

    public User(Integer intName, Integer intClass) {
        this.intName = intName;
        this.intClass = intClass;
    }

    private Integer intName;

    private Integer intClass;

    public Integer getIntName() {
        return intName;
    }

    public void setIntName(Integer intName) {
        this.intName = intName;
    }

    public Integer getIntClass() {
        return intClass;
    }

    public void setIntClass(Integer intClass) {
        this.intClass = intClass;
    }

    @Override
    public String toString() {
        return "User{" +
                "intName=" + intName +
                ", intClass=" + intClass +
                '}';
    }
}
