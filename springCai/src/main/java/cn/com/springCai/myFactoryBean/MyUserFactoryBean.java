package cn.com.springCai.myFactoryBean;

import cn.com.springCai.service.entity.people.po.People;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @Author caiJH
 * @Date 2025/3/17 9:16 AM
 * @Version 1.0
 */
@Component
public class MyUserFactoryBean implements FactoryBean<People> {

    /**
     * 创建对象的方式（new、反射、代理<jdk动态代理、CGLIB动态代理>）
     * @return
     * @throws Exception
     */
    @Override
    public People getObject() throws Exception {
        People people = new People();
        people.setName("CaiJinHui");
        people.setAddress("北京市，朝阳区");
        return people;
    }

    /**
     * 返回对象类型
     * @return
     */
    @Override
    public Class<?> getObjectType() {

        return People.class;
    }

    /**
     * 是否是单例的
     * @return
     */
    @Override
    public boolean isSingleton() {

        return true;
    }
}
