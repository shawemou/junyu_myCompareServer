package com.my.bean;

public class LoginBean {
	
	private String mobile;
	private String secret_key;
	private String type;
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getSecret_key() {
		return secret_key;
	}
	public void setSecret_key(String secretKey) {
		this.secret_key = secretKey;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "LoginBean [mobile=" + mobile + ", secret_key=" + secret_key + ", type=" + type + "]";
	}
	
}
