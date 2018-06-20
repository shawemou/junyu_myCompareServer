package com.my.bean;

public class PasswordBean extends LoginBean {
	private String new_secret_key;
	private String repeat_secret_key;
	
	public String getNew_secret_key() {
		return new_secret_key;
	}
	public void setNew_secret_key(String newSecretKey) {
		new_secret_key = newSecretKey;
	}
	public String getRepeat_secret_key() {
		return repeat_secret_key;
	}
	public void setRepeat_secret_key(String repeatSecretKey) {
		repeat_secret_key = repeatSecretKey;
	}
}
