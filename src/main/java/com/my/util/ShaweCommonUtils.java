package com.my.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.my.bean.LoginBean;
import com.my.common.BaseReturn;
import com.my.common.ReadSetting;
import com.shawemou.controller.ReadingSetting;

public class ShaweCommonUtils {
	
	public static boolean valiVersion(BaseReturn br,String version){
		
		Log4jUtil.log.warn("当前版本:" + ReadSetting.getInstance().getVersion());
		
		if( Check.IsStringNULL(version) ){
			br.success = false;
			br.version = true;
			br.info = "服务端接收信息不完整,请重试";
			Log4jUtil.log.warn("服务端接收信息不完整,请重试");
			
			return false;
		}else{
			if(!SpringBeanUtils.getBean2(ReadingSetting.class).getVersion().equals(decode(version))){
				br.success = false;
				br.version = false;
				br.info = "版本过低,请升级";
				Log4jUtil.log.warn("版本过低,请升级");
				return false;
			}
		}
		return true;
	}
	
	public static String decode(String reqString){
		if( !Check.IsStringNULL(reqString) ){
			try {
				return URLDecoder.decode(reqString,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return reqString;
	}

	public static BaseReturn validate(LoginBean loginBean) {

		BaseReturn br = new BaseReturn();
		if( Check.IsStringNULL(loginBean.getMobile())){
			br.setSuccess(false);
			br.setInfo("手机号不能为空");
			return br;
		}else if(loginBean.getMobile().length() < 11){
			br.setSuccess(false);
			br.setInfo("手机号不能少于11位");
			return br;
		}else if( Check.IsStringNULL(loginBean.getSecret_key()) ){
			br.setSuccess(false);
			br.setInfo("输入密码不能为空");
			return br;
		}
		return br;
	
	}
}
