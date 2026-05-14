package cn.com.springCai.aop;

import java.io.Serializable;

public class RestResult<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private String statusCode;
	private String msg;
	private T data;

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "RestResult [statusCode=" + statusCode + ", msg=" + msg + ", data=" + data + "]";
	}

}
