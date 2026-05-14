package cn.com.springCai.config;

import cn.com.springCai.aware.SpringAttribute;
import cn.com.springCai.service.entity.user.po.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author caiJH
 * @Date 2025/3/14 5:09 PM
 * @Version 1.0
 */
@Configuration
public class BeanConfig {

    @Bean
    public SpringAttribute springAttribute(){
        return new SpringAttribute();
    }

    @Bean
    public User user(){
        User user = new User();
        user.setName("蔡进辉");
        user.setMail("987903102@qq.com");
        return user;
    }
}
