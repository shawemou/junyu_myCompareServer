package com.my.validate;

import java.util.Map;

import com.my.common.BaseReturn;
import com.my.util.Check;

public class PasswordValidate {
	public static BaseReturn validate(Map<String,String> passwordMap){
		BaseReturn br = new BaseReturn();
		if( Check.IsStringNULL(passwordMap.get("mobile")) ){
			br.setSuccess(false);
			br.setInfo("手机号不能为空");
			return br;
		}else if(passwordMap.get("mobile").length() < 11){
			br.setSuccess(false);
			br.setInfo("手机号少于11位");
			return br;
		}else if( Check.IsStringNULL(passwordMap.get("secret_key")) ){
			br.setSuccess(false);
			br.setInfo("原密码不能为空");
			return br;
		}else if( Check.IsStringNULL(passwordMap.get("new_secret_key")) ){
			br.setSuccess(false);
			br.setInfo("新密码不能为空");
			return br;
		}else if( Check.IsStringNULL(passwordMap.get("repeat_secret_key")) ){
			br.setSuccess(false);
			br.setInfo("再次输入新密码不能为空");
			return br;
		}else if(!passwordMap.get("new_secret_key").equals(passwordMap.get("repeat_secret_key"))){
			br.setSuccess(false);
			br.setInfo("两次输入新密码不一致");
			return br;
		}
		return br;
	}
}
