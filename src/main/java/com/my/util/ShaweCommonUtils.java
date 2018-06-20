package com.my.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.my.bean.LoginBean;
import com.my.common.BaseReturn;
import com.my.common.ReadSetting;
import com.shawemou.controller.ReadingSetting;

public class ShaweCommonUtils {
	
	public static boolean valiVersion(BaseReturn br,String version){
		
		Log4jUtil.log.warn("��ǰ�汾:" + ReadSetting.getInstance().getVersion());
		
		if( Check.IsStringNULL(version) ){
			br.success = false;
			br.version = true;
			br.info = "����˽�����Ϣ������,������";
			Log4jUtil.log.warn("����˽�����Ϣ������,������");
			
			return false;
		}else{
			if(!SpringBeanUtils.getBean2(ReadingSetting.class).getVersion().equals(decode(version))){
				br.success = false;
				br.version = false;
				br.info = "�汾����,������";
				Log4jUtil.log.warn("�汾����,������");
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
			br.setInfo("�ֻ��Ų���Ϊ��");
			return br;
		}else if(loginBean.getMobile().length() < 11){
			br.setSuccess(false);
			br.setInfo("�ֻ��Ų�������11λ");
			return br;
		}else if( Check.IsStringNULL(loginBean.getSecret_key()) ){
			br.setSuccess(false);
			br.setInfo("�������벻��Ϊ��");
			return br;
		}
		return br;
	
	}
}
