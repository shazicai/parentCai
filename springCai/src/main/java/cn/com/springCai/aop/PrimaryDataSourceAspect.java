package cn.com.springCai.aop;

import cn.com.springCai.service.entity.student.po.Student;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @Author caiJH
 * @Date 2025/2/25 4:11 PM
 * @Version 1.0
 */
@Aspect
@Component
public class PrimaryDataSourceAspect {



    /**
     * 切service层
     * @param joinPoint
     */
//    @After("execution(* cn.com.springCai.service.primary..*.save(..))" +
//            "||execution(* cn.com.springCai.service.primary..*.update(..))")
//    public void logAfter(JoinPoint joinPoint){
//        Object target = joinPoint.getTarget();
//        System.out.println("主dataSource进入切面");
//    }

    /**
     * 切mapper/dao层
     * @return
     */
    @Around("(execution(* cn.com.springCai.mapper.primaryMapper..*.save(..))" +
            "||execution(* cn.com.springCai.mapper.primaryMapper..*.update(..)))" +
            "&&(@annotation(org.springframework.stereotype.Repository))")
    public Object operateAroundDao(ProceedingJoinPoint joinPoint){
        System.out.println("--------------dao层进入切面开始---------");
        Object[] args = joinPoint.getArgs();
        try {
            for (Object arg : args) {
                if(arg instanceof Student && "cjh".equals(((Student) arg).getName())){
                    Student student = (Student)arg;
                    student.setName("蔡进辉");
                }
            }
            joinPoint.proceed(args);
            System.out.println("---------dao层方法执行后---------");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return 22;
    }

//    @Around("(execution(* cn.com.springCai.service.primary..*.save(..))" +
//            "||execution(* cn.com.springCai.service.primary..*.update(..)))" +
//            "&&(@annotation(org.springframework.stereotype.Service))")
//    public Object operateAroundService(ProceedingJoinPoint joinPoint){
//        System.out.println("--------------service层进入切面开始---------");
//        Object[] args = joinPoint.getArgs();
//        try {
//            for (Object arg : args) {
//                if(arg instanceof Student && "cjh".equals(((Student) arg).getName())){
//                    Student student = (Student)arg;
//                    student.setName("蔡进辉");
//                }
//            }
//            joinPoint.proceed(args);
//            System.out.println("--------------service层进入切面结束---------");
//        } catch (Throwable e) {
//            throw new RuntimeException(e);
//        }
//        return 22;
//    }

}
