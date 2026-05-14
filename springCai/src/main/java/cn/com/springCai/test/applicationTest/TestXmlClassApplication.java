package cn.com.springCai.test.applicationTest;

import cn.com.springCai.test.applicationTest.applicationContext.MyAnnotationConfigApplicationContext;
import cn.com.springCai.test.applicationTest.applicationContext.MyClassPathXmlApplicationContext;
import cn.com.springCai.test.applicationTest.bean.TestService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;

/**
 * @Author caiJH
 * @Date 2025/3/27 2:21 PM
 * @Version 1.0
 */
public class TestXmlClassApplication {

    public static void main(String[] args) {
        TestXmlClassApplication testXmlClassApplication = new TestXmlClassApplication();


//        ApplicationContext applicationContext = testXmlClassApplication.testApplicationXml();

        //testXmlClassApplication自定义的applicationContext
//        ApplicationContext applicationContext = testXmlClassApplication.testMyApplicationXml();
        MyAnnotationConfigApplicationContext applicationContext = testXmlClassApplication.testMyAnnotationConfigApplicationContext();
        // 做一个钩子，执行正常销毁容器时的方法
        applicationContext.registerShutdownHook();
//        applicationContext.scan("/Users/caijinhui/IdeaProjects/project/parentCai/springCai/src/main/java/cn/com/springCai/config");
        applicationContext.refresh();
//        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(applicationContext.getResourceLoader());
//
        TestService testService = (TestService)applicationContext.getBean("testService");
        System.out.println(testService.toString());

        applicationContext.close();
    }

    /**
     * xml配置bean
     * @return
     */
    public ApplicationContext testApplicationXml(){
        return new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
    }

    /**
     * my xml配置bean
     * @return
     */
    public ApplicationContext testMyApplicationXml(){
        return new MyClassPathXmlApplicationContext("classpath:applicationContext.xml");
    }

    /**
     * 自定义注解ApplicationContext
     * @return
     */
    public MyAnnotationConfigApplicationContext testMyAnnotationConfigApplicationContext(){
        return new MyAnnotationConfigApplicationContext(AppConfig.class);
//        return new MyAnnotationConfigApplicationContext();
    }

}
