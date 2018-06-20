package com.my.validate;

import java.util.Map;

import com.my.common.CompareReturn;
import com.my.util.Check;

public class CompareValidate {
	
	public static CompareReturn validate(Map<String,String> compareMap){
		CompareReturn cr = new CompareReturn();
		if( Check.IsStringNULL(compareMap.get("mobile")) ){
			cr.setSuccess(false);
			cr.setInfo("�ֻ����벻��Ϊ��");
			return cr;
		}else if(compareMap.get("mobile").length() < 11){
			cr.setSuccess(false);
			cr.setInfo("�ֻ���������11λ");
			return cr;
		}else if( Check.IsStringNULL(compareMap.get("bar_code")) ){
			cr.setSuccess(false);
			cr.setInfo("�����벻��Ϊ��");
			return cr;
		}else if( Check.IsStringNULL(compareMap.get("id_type")) ){
			cr.setSuccess(false);
			cr.setInfo("֤�����Ͳ���Ϊ��");
			return cr;
		}else if( Check.IsStringNULL(compareMap.get("name")) ){
			cr.setSuccess(false);
			cr.setInfo("��������Ϊ��");
			return cr;
		}else if( Check.IsStringNULL(compareMap.get("id_number")) ){
			cr.setSuccess(false);
			cr.setInfo("֤�����벻��Ϊ��");
			return cr;
		}else if( Check.IsStringNULL(compareMap.get("photo_id")) ){
			cr.setSuccess(false);
			cr.setInfo("֤�������ղ���Ϊ��");
			return cr;
		}else if( Check.IsStringNULL(compareMap.get("photo_user")) ){
			cr.setSuccess(false);
			cr.setInfo("�û��ֳ��ղ���Ϊ��");
			return cr;
		}
		
		if(compareMap.get("name").indexOf(".") >= 0){
			compareMap.put("name", compareMap.get("name").replaceAll("\\.", "��"));
		}
		
		return cr;
	}
}
