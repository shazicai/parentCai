package cn.com.springCai.postProcessor;

import cn.com.springCai.aware.AttributeAware;
import cn.com.springCai.service.entity.user.po.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @Author caiJH
 * @Date 2025/3/14 5:16 PM
 * @Version 1.0
 */
@Component
public class AttributeAwareBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof AttributeAware){
            ((AttributeAware) bean).setAttribute((User) beanFactory.getBean("user"));
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
