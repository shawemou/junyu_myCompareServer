package com.my.validate;

import java.util.Map;

import com.my.common.BaseReturn;
import com.my.util.Check;

/**
 * @ClassName: LoginValidate 
 * @Description: �ֻ��û���¼��֤
 *
 */
public class LoginValidate {

	public static BaseReturn validate(Map<String,String> loginMap){
		BaseReturn br = new BaseReturn();
		if( Check.IsStringNULL(loginMap.get("mobile")) ){
			br.setSuccess(false);
			br.setInfo("�ֻ��Ų���Ϊ��");
			return br;
		}else if(loginMap.get("mobile").length() < 11){
			br.setSuccess(false);
			br.setInfo("�ֻ��Ų�������11λ");
			return br;
		}else if( Check.IsStringNULL(loginMap.get("secret_key")) ){
			br.setSuccess(false);
			br.setInfo("�������벻��Ϊ��");
			return br;
		}
		return br;
	}
}

