package com.sherlock.imapp.common;

public class Result {

	public static final Integer SUCCESS_CODE = 1;
	public static final Integer EXCEPTION_CODE = 100;
	private Integer code;
	
	private String msg;
	
	private Object data;

	public static Result success(Object data){
		Result result = new Result();
		result.setCode(SUCCESS_CODE);
		result.setData(data);
		return result;
	}
	
	public static Result success(String msg, Object data){
		Result result = new Result();
		result.setCode(SUCCESS_CODE);
		result.setMsg(msg);
		result.setData(data);
		return result;
	}
	
	public static Result exception(String msg){
		Result result = new Result();
		result.setCode(EXCEPTION_CODE);
		result.setMsg(msg);
		return result;
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
