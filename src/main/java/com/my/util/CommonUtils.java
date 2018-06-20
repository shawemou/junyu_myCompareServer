package com.my.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.my.common.BaseReturn;
import com.my.common.ReadSetting;

public class CommonUtils {
	
	
	
	/*public static boolean valiVersion(HttpServletRequest request, BaseReturn br){
		Log4jUtil.log.warn("手机号:" + request.getParameter("mobile") + ";type:" + request.getParameter("type") + ";version:" + request.getParameter("version"));
		Log4jUtil.log.warn("当前版本:" + ReadSetting.getInstance().getVersion());
		
		String version = request.getParameter("version");
		if( Check.IsStringNULL(version) ){
			br.success = false;
			br.version = true;
			br.info = "服务端接收信息不完整,请重试";
			Log4jUtil.log.warn("服务端接收信息不完整,请重试");
			
			return false;
		}else{
			if(!ReadSetting.getInstance().getVersion().equals(decode(version))){
				br.success = false;
				br.version = false;
				br.info = "版本过低,请升级";
				Log4jUtil.log.warn("版本过低,请升级");
				return false;
			}
		}
		return true;
	}*/
	
	public static boolean valiVersion(HttpServletRequest request, BaseReturn br,String version){
		Log4jUtil.log.warn("手机号:" + request.getParameter("mobile") + ";type:" + request.getParameter("type") + ";version:" + version);
		Log4jUtil.log.warn("当前版本:" + ReadSetting.getInstance().getVersion());
		
		if( Check.IsStringNULL(version) ){
			br.success = false;
			br.version = true;
			br.info = "服务端接收信息不完整,请重试";
			Log4jUtil.log.warn("服务端接收信息不完整,请重试");
			
			return false;
		}else{
			if(!ReadSetting.getInstance().getVersion().equals(decode(version))){
				br.success = false;
				br.version = false;
				br.info = "版本过低,请升级";
				Log4jUtil.log.warn("版本过低,请升级");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 从request根据javabean的属性,获取值，放入Map中
	 * @return
	 */
	public static Map<String,String> setBeanValue(Class<?> clazz,HttpServletRequest request){
		BeanInfo beaninfo = null;
		Map<String,String> map = new HashMap<String,String>();
		try {
			beaninfo = Introspector.getBeanInfo(clazz);
			PropertyDescriptor[] pr = beaninfo.getPropertyDescriptors();
			for(int i = 0; i < pr.length;i++){
				if(!pr[i].getName().toString().equals("class"))
					map.put(pr[i].getName().toString(),decode(getRequestString(request, pr[i].getName().toString())));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 从request获取参数的值
	 * @return
	 */
	public static String getRequestString(HttpServletRequest request,String name){		
		if(request != null){
			return request.getParameter(name) == null ? "" : request.getParameter(name).equals("undefined")?"":request.getParameter(name);
		}else{			
			return "";
		}
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

}
