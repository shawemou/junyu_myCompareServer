package com.my.validate;

import java.util.Map;

import com.my.common.BaseReturn;
import com.my.util.Check;

public class CompleteValidate {
	public static BaseReturn validate(Map<String,String> compareMap){
		BaseReturn cr = new BaseReturn();
		if( Check.IsStringNULL(compareMap.get("bar_code")) ){
			cr.setSuccess(false);
			cr.setInfo("�����벻��Ϊ��");
			return cr;
		}
		return cr;
	}
}
