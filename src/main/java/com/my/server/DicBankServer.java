package com.my.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.dao.WebMobileDao;
import com.my.servlet.InitServlet;
import com.my.util.Check;
import com.my.util.Log4jUtil;

@Service
public class DicBankServer {
	
	@Autowired
	private WebMobileDao webMobileDao;
	
	/**
	 * 获取银行名称列表
	 * @param bankMap
	 * @return
	 */
	public String getDicBank(Map<String,String> bankMap){
		Log4jUtil.log.warn(bankMap.get("bank_name"));
		return setBsnkJsonList( bankList(bankMap) ).toString();
	}
	
	/**
	 * 获取银行字典
	 * @return
	 */
	public List<Map<String, Object>> bankList(Map<String,String> mobileMap){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(InitServlet.bankList == null){
			InitServlet.bankList = webMobileDao.bankList();
		}
		if( !Check.IsStringNULL(mobileMap.get("bank_name")) ){
			for(Map<String,Object> map : InitServlet.bankList){
				if(map.get("BANK_NAME").toString().toUpperCase().indexOf(mobileMap.get("bank_name").toUpperCase()) >= 0){
					list.add(map);
				}
			}
		}else{
			list = InitServlet.bankList;
		}
		return list;
	}
	
	public static org.json.JSONArray setBsnkJsonList(List<Map<String, Object>> list){
		org.json.JSONArray jary = new org.json.JSONArray();
		for(Map<String,Object> map : list){
			Map<String,Object> map_tem = new HashMap<String,Object>();
			map_tem.put("value", map.get("BANK_NAME"));
			jary.put(map_tem);
		}
		return jary;
	}
}
