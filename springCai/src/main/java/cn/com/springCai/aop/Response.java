package cn.com.springCai.aop;

/**
 * @Author caiJH
 * @Date 2025/9/16 4:47 PM
 * @Version 1.0
 */
public class Response<T> {

    private T data;

    private Integer code;

    private String msg;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
