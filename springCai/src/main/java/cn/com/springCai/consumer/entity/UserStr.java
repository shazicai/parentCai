package cn.com.springCai.consumer.entity;

import java.io.Serializable;

/**
 * @Author caiJH
 * @Date 2025/6/27 3:47 PM
 * @Version 1.0
 */
public class UserStr implements Serializable {

    private static final long serialVersionUID = -4395549708998684525L;

    private String name;

    private String className;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "UserStr{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
