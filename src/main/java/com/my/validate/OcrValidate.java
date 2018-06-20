package com.my.validate;

import java.util.Map;

import com.my.common.OcrReturn;
import com.my.util.Check;

public class OcrValidate {
	public static OcrReturn validate(Map<String,String> ocrMap){
		OcrReturn or = new OcrReturn();
		if( Check.IsStringNULL(ocrMap.get("mobile")) ){
			or.setSuccess(false);
			or.setInfo("手机号码不能为空");
			return or;
		}else if(ocrMap.get("mobile").length() < 11){
			or.setSuccess(false);
			or.setInfo("手机号码少于11位");
			return or;
		}else if( Check.IsStringNULL(ocrMap.get("bar_code")) ){
			or.setSuccess(false);
			or.setInfo("条形码不能为空");
			return or;
		}else if( Check.IsStringNULL(ocrMap.get("id_photo")) ){
			or.setSuccess(false);
			or.setInfo("身份证照片不能为空");
			return or;
		}
		return or;
	}
}
