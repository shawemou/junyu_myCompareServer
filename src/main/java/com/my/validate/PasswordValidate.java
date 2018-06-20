package com.my.validate;

import java.util.Map;

import com.my.common.BaseReturn;
import com.my.util.Check;

public class PasswordValidate {
	public static BaseReturn validate(Map<String,String> passwordMap){
		BaseReturn br = new BaseReturn();
		if( Check.IsStringNULL(passwordMap.get("mobile")) ){
			br.setSuccess(false);
			br.setInfo("�ֻ��Ų���Ϊ��");
			return br;
		}else if(passwordMap.get("mobile").length() < 11){
			br.setSuccess(false);
			br.setInfo("�ֻ�������11λ");
			return br;
		}else if( Check.IsStringNULL(passwordMap.get("secret_key")) ){
			br.setSuccess(false);
			br.setInfo("ԭ���벻��Ϊ��");
			return br;
		}else if( Check.IsStringNULL(passwordMap.get("new_secret_key")) ){
			br.setSuccess(false);
			br.setInfo("�����벻��Ϊ��");
			return br;
		}else if( Check.IsStringNULL(passwordMap.get("repeat_secret_key")) ){
			br.setSuccess(false);
			br.setInfo("�ٴ����������벻��Ϊ��");
			return br;
		}else if(!passwordMap.get("new_secret_key").equals(passwordMap.get("repeat_secret_key"))){
			br.setSuccess(false);
			br.setInfo("�������������벻һ��");
			return br;
		}
		return br;
	}
}
