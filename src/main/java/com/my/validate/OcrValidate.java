package com.my.validate;

import java.util.Map;

import com.my.common.OcrReturn;
import com.my.util.Check;

public class OcrValidate {
	public static OcrReturn validate(Map<String,String> ocrMap){
		OcrReturn or = new OcrReturn();
		if( Check.IsStringNULL(ocrMap.get("mobile")) ){
			or.setSuccess(false);
			or.setInfo("�ֻ����벻��Ϊ��");
			return or;
		}else if(ocrMap.get("mobile").length() < 11){
			or.setSuccess(false);
			or.setInfo("�ֻ���������11λ");
			return or;
		}else if( Check.IsStringNULL(ocrMap.get("bar_code")) ){
			or.setSuccess(false);
			or.setInfo("�����벻��Ϊ��");
			return or;
		}else if( Check.IsStringNULL(ocrMap.get("id_photo")) ){
			or.setSuccess(false);
			or.setInfo("���֤��Ƭ����Ϊ��");
			return or;
		}
		return or;
	}
}
