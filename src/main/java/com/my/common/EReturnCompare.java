package com.my.common;

import java.util.HashMap;
import java.util.Map;

public class EReturnCompare {
	public static Map<String,String> map = new HashMap<String,String>();
	static {
		map.put("0", "ϵͳ�ж�Ϊͬһ��");
		map.put("1", "ϵͳ����ȷ���Ƿ�Ϊͬһ��");
		map.put("2", "ϵͳ�ж�Ϊ��ͬ��");
		
		map.put("-4", "������,�������ϸ�");
		map.put("-7", "������,��ʽ����ȷ");
		map.put("-6", "������,�������ʧ��");
		map.put("-9", "������,��⵽��������");
		
		map.put("-3", "�ֳ���,�������ϸ�");
		map.put("-8", "�ֳ���,��ʽ����ȷ");
		map.put("-2", "�ֳ���,�������ʧ��");
		map.put("-10", "�ֳ���,��⵽��������");
		
		map.put("-5", "�������ڲ�����");
		map.put("2000", "�������ڲ�����");
		map.put("12002", "�������ڲ�����");
		map.put("-1300", "�������ڲ�����");
		map.put("-1301", "���������ӳ�ʱ");
	}
}
