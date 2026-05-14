package cn.com.springCai.condition;


import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Author caiJH
 * @Date 2025/4/28 3:39 PM
 * @Version 1.0
 * desc Condition条件设置
 */
public class UserConditional implements Condition {

    /**
     * 在需要交给spring容器管理的类上加上这个条件，matches函数会加载UserCondition
     * 如果成功，则加了UserConditional条件的类可以成为一个bean，
     * 否则，此类不能成为一个bean
     * @param context the condition context
     * @param metadata metadata of the {@link org.springframework.core.type.AnnotationMetadata class}
     * or {@link org.springframework.core.type.MethodMetadata method} being checked.
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        try {
            Class<?> aClass = context.getClassLoader().loadClass("cn.com.springCai.condition.UserCondition");
            return true;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
