package com.my.bean;

public class WebStatBean {

	public String beginDate;
	public String endDate;
	public String bankName;
	
	private String page_no;
	private String page_count;
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getPage_no() {
		return page_no;
	}
	public void setPage_no(String pageNo) {
		page_no = pageNo;
	}
	public String getPage_count() {
		return page_count;
	}
	public void setPage_count(String pageCount) {
		page_count = pageCount;
	}
}
