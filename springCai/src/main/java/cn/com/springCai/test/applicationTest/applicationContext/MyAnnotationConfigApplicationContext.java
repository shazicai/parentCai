package cn.com.springCai.test.applicationTest.applicationContext;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author caiJH
 * @Date 2025/4/22 8:56 AM
 * @Version 1.0
 */
public class MyAnnotationConfigApplicationContext extends AnnotationConfigApplicationContext {
    public MyAnnotationConfigApplicationContext() {
        super();
    }

    public MyAnnotationConfigApplicationContext(DefaultListableBeanFactory beanFactory) {
        super(beanFactory);
    }

    public MyAnnotationConfigApplicationContext(Class<?>... annotatedClasses) {
        super(annotatedClasses);
    }

    public MyAnnotationConfigApplicationContext(String... basePackages) {
        super(basePackages);
    }
}
