package cn.com.springCai.service.entity.user.po;


import java.io.Serializable;


public class User implements Serializable {

    /**
     * 姓名
     */
    private String name;

    /**
     * 邮箱
     */
    private String mail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
