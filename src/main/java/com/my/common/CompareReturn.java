package com.my.common;

import java.util.Date;

public class CompareReturn extends BaseReturn {

	public int similarity;
	public String code;
	public String return_code;
	public Date beginDate = new Date();
	
	public String sCode;//��Դ��֤������
	public String sInfo;//��Դ��֤��������
	public Long sholdTime;//��Դ��֤��ʱ
	
	public int getSimilarity() {
		return similarity;
	}
	public void setSimilarity(int similarity) {
		this.similarity = similarity;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String returnCode) {
		this.return_code = returnCode;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public String getsCode() {
		return sCode;
	}
	public void setsCode(String sCode) {
		this.sCode = sCode;
	}
	public String getsInfo() {
		return sInfo;
	}
	public void setsInfo(String sInfo) {
		this.sInfo = sInfo;
	}
	public Long getSholdTime() {
		return sholdTime;
	}
	public void setSholdTime(Long sholdTime) {
		this.sholdTime = sholdTime;
	}
}
