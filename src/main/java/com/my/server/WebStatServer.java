package com.my.server;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.dao.WebStatDao;
import com.my.util.Check;

/**
 * @ClassName: WebStatServer 
 * @Description: ͳ��һ��ʱ���ڵİ����� server�� ��ҳ��ʾ
 * @author lulinlin
 * @date 2017-2-14 ����04:38:39 
 */
@Service
public class WebStatServer {
	
	@Autowired
	private WebStatDao webStatDao;
	
	public String methed(String method, Map<String,String> statMap,HttpServletRequest request){
		JSONObject resultJson = new JSONObject();
		try{
			if( !Check.IsStringNULL(method) ){
				if( method.equals("list") ){//�б�
					String page_no = statMap.get("page_no");
					if(Check.BeginIndex(page_no) <= 0){
						statMap.put("page_no","1");
					}
					resultJson.put("data", setJsonList( list(statMap) ));
					resultJson.put("itemCount",queryCount(statMap) );
					resultJson.put("page_no",statMap.get("page_no") );
				}else{
					resultJson.put("success",false);
					resultJson.put("info","���õ�ַ������");
				}
			}else{
				resultJson.put("success",false);
				resultJson.put("info","���õ�ַ������");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return resultJson.toString();
	}
	
	/**
	 * ��ȡ�б�
	 * @param mobileMap
	 * @return
	 */
	public List<Map<String, Object>> list(Map<String,String> statMap){
		return webStatDao.list(statMap);
	}
	
	public int queryCount(Map<String,String> statMap){
		return webStatDao.queryCount(statMap);
	}
	
	public static org.json.JSONArray setJsonList(List<Map<String,Object>> list){
		org.json.JSONArray jary = new org.json.JSONArray();
		for(Map<String,Object> map : list){
			jary.put(map);
		}
		return jary;
	}
}
