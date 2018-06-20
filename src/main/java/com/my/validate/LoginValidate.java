package com.my.validate;

import java.util.Map;

import com.my.common.BaseReturn;
import com.my.util.Check;

/**
 * @ClassName: LoginValidate 
 * @Description: 手机用户登录验证
 *
 */
public class LoginValidate {

	public static BaseReturn validate(Map<String,String> loginMap){
		BaseReturn br = new BaseReturn();
		if( Check.IsStringNULL(loginMap.get("mobile")) ){
			br.setSuccess(false);
			br.setInfo("手机号不能为空");
			return br;
		}else if(loginMap.get("mobile").length() < 11){
			br.setSuccess(false);
			br.setInfo("手机号不能少于11位");
			return br;
		}else if( Check.IsStringNULL(loginMap.get("secret_key")) ){
			br.setSuccess(false);
			br.setInfo("输入密码不能为空");
			return br;
		}
		return br;
	}
}

