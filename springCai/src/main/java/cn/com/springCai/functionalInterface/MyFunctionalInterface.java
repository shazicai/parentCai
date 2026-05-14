package cn.com.springCai.functionalInterface;

/**
 * @Author caiJH
 * @Date 2025/3/21 9:21 AM
 * @Version 1.0
 */
@FunctionalInterface
public interface MyFunctionalInterface<T> {

    T getObject() throws Exception;
}
