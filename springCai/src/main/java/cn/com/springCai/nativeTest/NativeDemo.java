package cn.com.springCai.nativeTest;

/**
 * @Author caiJH
 * @Date 2025/6/24 4:05 PM
 * @Version 1.0
 */
public class NativeDemo {
    //申明本地方法
    public native void printMessage();

    static {
        System.loadLibrary("NativeLibrary");
    }

}
