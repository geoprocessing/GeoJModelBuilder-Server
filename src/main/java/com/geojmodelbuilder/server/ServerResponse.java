package com.geojmodelbuilder.server;

public class ServerResponse {
	
	/**
	 * 200 success
	 * 400 failure
	 */
	public int status;
	public String msg;
	public Object detail;
	
	public ServerResponse(int status, String msg, Object detail){
		this.status = status;
		this.msg = msg;
		this.detail = detail;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getDetail() {
		return detail;
	}
	public void setDetail(Object detail) {
		this.detail = detail;
	}
}
