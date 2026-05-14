package cn.com.springCai.test.applicationTest.applicationContext;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URL;

/**
 * @Author caiJH
 * @Date 2025/4/3 2:32 PM
 * @Version 1.0
 */
public class MyClassPathXmlApplicationContext extends ClassPathXmlApplicationContext {

    public MyClassPathXmlApplicationContext(String configLocation ) {
        super(configLocation);
        test();
    }

    @Override
    protected void initPropertySources() {
        System.out.println("扩展函数initPropertySources");
        getEnvironment().setRequiredProperties("LOGNAME");
        System.out.println("12312");
    }

    @Override
    protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
        // 设置是否可覆盖同名称的不同定义对象
        beanFactory.setAllowBeanDefinitionOverriding(false);
        // 设置是否允许bean之间的循环依赖
        beanFactory.setAllowCircularReferences(false);
        super.customizeBeanFactory(beanFactory);
    }

    public void test(){
        ClassLoader classLoader = this.getClassLoader();
        URL resource = classLoader.getResource("cn/com/springCai/aop");
    }
}
