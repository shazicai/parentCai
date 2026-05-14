package cn.com.springCai.test;

import java.io.Serializable;

public class ServiceResult implements Serializable{

	private static final long serialVersionUID = 1L;


	/**
     * 操作状态
     */
    private boolean status =false;


    /**
     *操作返回结果
     */
    private Object result;

    
    private String msg;

	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	public boolean getStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}


	public Object getResult() {
		return result;
	}


	public void setResult(Object result) {
		this.result = result;
	}
}