package com.hackathon.data;

public class CreditResponse {

	
	
	String statuscode;
	String data;
	String errorinfo;
	String url;
	public String getStatuscode() {
		return statuscode;
	}
	public void setStatuscode(String statuscode) {
		this.statuscode = statuscode;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getErrorinfo() {
		return errorinfo;
	}
	public void setErrorinfo(String errorinfo) {
		this.errorinfo = errorinfo;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public CreditResponse(String statuscode, String data, String errorinfo, String url) {
		super();
		this.statuscode = statuscode;
		this.data = data;
		this.errorinfo = errorinfo;
		this.url = url;
	}
	public CreditResponse() {
		super();
	}
	
	
	
}
