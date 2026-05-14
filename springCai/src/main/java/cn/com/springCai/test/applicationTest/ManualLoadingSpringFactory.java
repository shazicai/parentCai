package cn.com.springCai.test.applicationTest;

import cn.com.springCai.test.applicationTest.bean.TestService;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListenerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @Author caiJH
 * @Date 2025/4/21 10:06 AM
 * @Version 1.0
 */
public class ManualLoadingSpringFactory {

    /**
     * 创建容器 ——> 读取配置 ——>(标准化接口BeanDefinitionReader) 解析xml、注解等方式 ——>
     * @param args
     */
    public static void main(String[] args) {
        // 手动完成整个容器加载
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 读取配置文件 xml方式（标准化接口BeanDefinitionReader）,并封装成beanDefinition
//        XmlBeanDefinitionReader definitionReader = new XmlBeanDefinitionReader(beanFactory);
//        definitionReader.loadBeanDefinitions(new ClassPathResource("applicationContext.xml"));

        /**
         * 下面是注解方式
         * 1、创建数据读取器
         * 2、将读取到的配置文件类放入beanDefinition
         * 3、再解析配置文件的beanDefinition，查看其所有的注解类
         * 4、通过ASM（ASM（Java 字节码操作框架） 的字节码操作技术）读取配置文件类的信息
         * 5、根据配置类去扫描或其他操作，将Component加载成beanDefinition
         * 6、走bean的生命周期流程lifeCycle
         */
        // 读取配置文件 注解方式方式（标准化接口BeanDefinitionReader）,并封装成beanDefinition，此时需要注意的是
        // 现将配置文件类解析成了一个beanDefinition
        AnnotatedBeanDefinitionReader annotatedBeanDefinitionReader = new AnnotatedBeanDefinitionReader(beanFactory);
        annotatedBeanDefinitionReader.register(MainStart.class);
        // MainStart注册成一个beanDefinition，再解析配置文件里面的数据
        AnnotatedGenericBeanDefinition mainStartBeanDefinition = (AnnotatedGenericBeanDefinition) beanFactory.getBeanDefinition("mainStart");
        // 获取类的原数据（ASM（Java 字节码操作框架） 的字节码操作技术）读取配置文件类的信息
        AnnotationMetadata metadata = mainStartBeanDefinition.getMetadata();
        String[] basePackagesValue = null;
        if(metadata.hasAnnotation(ComponentScan.class.getName())){
            Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(ComponentScan.class.getName());
            basePackagesValue = (String[])annotationAttributes.get("basePackages");
        }
        // 扫描器(扫描位置)生成beanDefinition
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);
        for (String str : basePackagesValue) {
            scanner.scan(str);
        }
        // 创建所用bean 实例化所有的单例对象 报错，EventListenerMethodProcessor的ConfigurableApplicationContext为空，无法获取到
        // applicationContext.getBeansOfType(EventListenerFactory.class)报错
//        beanFactory.preInstantiateSingletons();


        // 获取对象 （扫描不会创建bean对象，beanFactory.preInstantiateSingletons()实例化所有的bean）
        TestService testService = (TestService)beanFactory.getBean("testService");
        System.out.println(testService.toString());

    }
}
