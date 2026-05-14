package com.spc.springweb.base;

/**
 * api响应实体
 * 
 * @author Zouxf
 *
 */
public class APIResponse {
	/**
	 * 返回码
	 */
	private int statusCode;
	/**
	 * 返回信息
	 */
	private String msg;
	/**
	 * 返回数据
	 */
	private Object data;

	public APIResponse() {
		this(ApiResultCode.SUCCESS, null, null);
	}

	public APIResponse(Object result) {
		this(200, null, result);
	}

	public APIResponse(int statusCode) {
		this(statusCode, null, null);
	}

	public APIResponse(int statusCode, String msg) {
		this(statusCode, msg, null);
	}

	public APIResponse(int statusCode, String msg, Object data) {
		this.statusCode = statusCode;
		this.msg = msg;
		this.data = data;
	}

	public static final APIResponse success(Object result) {
		APIResponse apiResponse = new APIResponse(result);
		return apiResponse;
	}

	public static final APIResponse fail(int statusCode) {
		return fail(statusCode, null);
	}

	public static final APIResponse fail(int statusCode, String msg) {
		APIResponse apiResponse = new APIResponse(statusCode, msg);
		return apiResponse;
	}

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
		return "APIResponse [statusCode=" + statusCode + ", msg=" + msg + ", data=" + data + "]";
	}

}
