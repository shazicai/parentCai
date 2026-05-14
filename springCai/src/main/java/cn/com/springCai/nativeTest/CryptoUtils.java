package cn.com.springCai.nativeTest;

/**
 * @Author caiJH
 * @Date 2025/6/24 4:40 PM
 * @Version 1.0
 */
public class CryptoUtils {

    /**
     * 声明本地方法
     * @param data
     * @param key
     * @return
     */
    public native byte[] encrypt(byte[] data, String key);

    private native void sayHell();

    static {
        System.loadLibrary("crypto_jni");
        System.loadLibrary("hello");
    }
}
