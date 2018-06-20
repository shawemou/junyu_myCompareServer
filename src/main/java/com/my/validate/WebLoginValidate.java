package com.my.validate;

import com.my.common.BaseReturn;
import com.my.util.Check;

public class WebLoginValidate {
	
	public static BaseReturn validate(String login_name, String login_psd){
		BaseReturn br = new BaseReturn();
		if( Check.IsStringNULL(login_name) ){
			br.setSuccess(false);
			br.setInfo("登录名为空");
			return br;
		}else if( login_name.length() < 5 || login_name.length() > 18 ){
			br.setSuccess(false);
			br.setInfo("登录名长度不正确");
			return br;
		}else if( Check.IsStringNULL(login_psd) ){
			br.setSuccess(false);
			br.setInfo("登录密码为空");
			return br;
		}else if( login_psd.length() < 5 || login_psd.length() > 18 ){
			br.setSuccess(false);
			br.setInfo("登录密码长度不正确");
			return br;
		}
		return br;
	}
}
