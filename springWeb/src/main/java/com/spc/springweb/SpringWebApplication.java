package com.spc.springweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:dubbo-consumer.xml")
public class SpringWebApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpringWebApplication.class, args);
        System.out.println("\n\n*********************springWeb服务发布成功！*********************\n\n");
    }

}
