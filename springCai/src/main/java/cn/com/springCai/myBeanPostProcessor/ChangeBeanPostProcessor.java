package cn.com.springCai.myBeanPostProcessor;

import cn.com.springCai.myAnnotation.GetValue;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * @Author caiJH
 * @Date 2025/3/12 11:17 AM
 * @Version 1.0
 */
@Component
public class ChangeBeanPostProcessor implements BeanPostProcessor {

    private static final String prefix = "${";

    private static final String suffix = "}";

    @Autowired
    private Environment env;


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if(beanName.equals("studentService")){
            System.out.printf("postProcessBeforeInitialization 函数---%s\n", beanName);
        }
        return bean;
    }

    @SneakyThrows
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            GetValue annotation = field.getAnnotation(GetValue.class);
            if(!ObjectUtils.isEmpty(annotation) && StringUtils.hasLength(annotation.value())){
                System.out.printf("有GetValue注解的bean为:%s, value:%s--去掉prefix和suffix后:%s\n",beanName,annotation.value(),annotation.value().replace(prefix,"").replace(suffix,""));
                String value = env.getProperty(annotation.value().replace(prefix, "").replace(suffix, ""));
                field.setAccessible(true);
                try {
                    field.set(bean,value);
                }catch (IllegalAccessException e){
                    throw new RuntimeException(e);
                }

            }
        }
        return bean;
    }
}
