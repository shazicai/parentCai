package cn.com.springCai.myBeanFactoryPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @Author caiJH
 * @Date 2025/3/12 11:16 AM
 * @Version 1.0
 */
@Component
public class ChangeMyAnnotationValue implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (int i = 0; i < beanDefinitionNames.length; i++) {
            if(beanDefinitionNames[i].equals("primarySqlSessionTemplate") || beanDefinitionNames[i].equals("secondarySqlSessionTemplate")){
                System.out.printf("beanDefinition的名称:%s\n",beanDefinitionNames[i]);
            }
        }
    }
}
