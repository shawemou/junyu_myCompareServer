package com.my.bean;

public class CompareBean {
	
	private String mobile;
	private String bar_code;
	private String name;
	private String id_number;
	private String id_type;//0其他证件，1身份证
	
	private String photo_id;
	private String photo_user;
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getBar_code() {
		return bar_code;
	}
	public void setBar_code(String barCode) {
		this.bar_code = barCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId_number() {
		return id_number;
	}
	public void setId_number(String idNumber) {
		this.id_number = idNumber;
	}
	public String getPhoto_id() {
		return photo_id;
	}
	public void setPhoto_id(String photoId) {
		this.photo_id = photoId;
	}
	public String getPhoto_user() {
		return photo_user;
	}
	public void setPhoto_user(String photoUser) {
		this.photo_user = photoUser;
	}
	public String getId_type() {
		return id_type;
	}
	public void setId_type(String idType) {
		this.id_type = idType;
	}
}
