package com.my.common;

public class BaseReturn {

	public boolean success = true;
	public boolean version = true;
	public String info;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public boolean isVersion() {
		return version;
	}
	public void setVersion(boolean version) {
		this.version = version;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
}
