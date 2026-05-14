package cn.com.springCai.myAnnotation;

import java.lang.annotation.*;

/**
 * @Author caiJH
 * @Date 2025/3/12 1:47 PM
 * @Version 1.0
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GetValue {
    String value();
}
