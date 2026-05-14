package cn.com.springCai.service.entity.people.po;

import java.io.Serializable;

/**
 * @Author caiJH
 * @Date 2025/3/17 9:43 AM
 * @Version 1.0
 */

public class People implements Serializable {

    private static final long serialVersionUID = -5782118659623527304L;

    private String name;

    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
