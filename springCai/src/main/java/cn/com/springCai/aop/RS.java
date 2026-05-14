package cn.com.springCai.aop;

import com.alibaba.fastjson2.annotation.JSONField;

public class RS {

	
	/**
	 * 状态码
	 */
	@JSONField(name="statusCode")
	private int statusCode;
	
	/**
	 * 相应信息
	 */
	@JSONField(name="msg")
	private String msg;
	
	private Object data;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "RS [statusCode=" + statusCode + ", msg=" + msg + ", data=" + data + "]";
	}
}
