package cn.com.springCai.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @Author caiJH
 * @Date 2025/2/25 4:11 PM
 * @Version 1.0
 */
@Aspect
@Component
public class SecondaryDataSourceAspect {

    @After("execution(* cn.com.springCai.service.secondary..*.save(..)) || " +
            "execution(* cn.com.springCai.service.secondary..*.update(..))")
    public void logAfter(JoinPoint joinPoint){
        System.out.println("辅助dataSource进入切面");
    }
}
