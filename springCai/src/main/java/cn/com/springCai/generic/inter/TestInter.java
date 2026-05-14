package cn.com.springCai.generic.inter;

/**
 * @Author caiJH
 * @Date 2025/5/30 11:13 AM
 * @Version 1.0
 */
public interface TestInter<T,V> {

    T getT(V v);

    V getV(T t);

}
