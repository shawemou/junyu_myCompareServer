package com.my.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.dao.WebMobileDao;
import com.my.servlet.InitServlet;
import com.my.util.AESCodec;
import com.my.util.Check;
import com.my.util.Log4jUtil;


@Service
public class WebMobileServer {
	
	@Autowired
	private  WebMobileDao webMobileDao;
	
	public String methed(String method, Map<String,String> mobileMap,HttpServletRequest request){
		JSONObject resultJson = new JSONObject();
		try{
			if( !Check.IsStringNULL(method) ){
				if( method.equals("list") ){//�б�
					String page_no = mobileMap.get("page_no");
					if(Check.BeginIndex(page_no) <= 0){
						mobileMap.put("page_no","1");
					}
					resultJson.put("data", setJsonList( list(mobileMap) ));
					resultJson.put("itemCount",queryCount(mobileMap) );
					resultJson.put("page_no",mobileMap.get("page_no") );
				}else if( method.equals("loadMobile") ){//��ȡ��������
					resultJson = setJsonMap(loadMobile(mobileMap));
				}else if( method.equals("saveMobile") ){//����
					Map<String, Object> userMap = (Map<String, Object>) request.getSession().getAttribute("user");
					if( Check.IsStringNULL(mobileMap.get("mobile")) ){
						resultJson.put("success",false);
						resultJson.put("info","�ֻ��������");
					}else{
						int i = saveMobile(mobileMap, userMap);//0ʧ�ܣ�1�ɹ� ��2�Ѵ���
						if(i == 0){
							resultJson.put("success",false);
							resultJson.put("info","����ʧ��");
						}else if(i == 1){
							setBankList(mobileMap);
							resultJson.put("success",true);
							resultJson.put("info","����ɹ�");
						}else if(i == 2){
							resultJson.put("success",false);
							resultJson.put("info","�ֻ������Ѵ����޷�����");
						}
					}
				}else if( method.equals("updateMobile") ){//�޸�
					if( Check.IsStringNULL(mobileMap.get("guid")) ){
						resultJson.put("success",false);
						resultJson.put("info","��Ϣȱʧ,�޷����");
					}else if( Check.IsStringNULL(mobileMap.get("mobile")) ){
						resultJson.put("success",false);
						resultJson.put("info","�ֻ��������");
					}else{
						int i = updateMobile(mobileMap);
						if(i == 0){
							resultJson.put("success",false);
							resultJson.put("info","����ʧ��");
						}else if(i == 1){
							setBankList(mobileMap);
							resultJson.put("success",true);
							resultJson.put("info","����ɹ�");
						}else if(i == 2){
							resultJson.put("success",false);
							resultJson.put("info","�ֻ������Ѵ����޷�����");
						}
					}
				}else if( method.equals("updateBusable") ){//���״̬
					if( Check.IsStringNULL(mobileMap.get("guid")) ){
						resultJson.put("success",false);
						resultJson.put("info","��Ϣȱʧ,�޷����");
					}else{
						int i = updateBusable(mobileMap.get("guid"));
						if(i == 1 || i == 0){
							resultJson.put("success",true);
							resultJson.put("info",i);
						}else{
							resultJson.put("success",false);
							resultJson.put("info",i);
						}
					}
				}else if(method.equals("updatePwd") ){//��������
					if( Check.IsStringNULL(mobileMap.get("guid")) ){
						resultJson.put("success",false);
						resultJson.put("info","��Ϣȱʧ,�޷���������");
					}else{
						if( updatePwd(mobileMap.get("guid")) > 0){
							resultJson.put("success",true);
							resultJson.put("info","��������ɹ�");
						}else{
							resultJson.put("success",false);
							resultJson.put("info","��������ʧ��");
						}
					}
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
	public List<Map<String, Object>> list(Map<String,String> mobileMap){
		List<Map<String, Object>> list = webMobileDao.list(mobileMap);
		for(Map<String, Object> map : list){
			if(map.get("SECRET_TYPE") != null && map.get("SECRET_TYPE").toString().equals("2")){
				map.put("SECRET_KEY", "�������޸�");
			}else{
				map.put("SECRET_KEY", AESCodec.aesDecrypt(map.get("SECRET_KEY").toString()));
			}
		}
		return list;
	}
	
	public int queryCount(Map<String,String> mobileMap){
		return webMobileDao.queryCount(mobileMap);
	}
	
	public Map<String, Object> loadMobile(Map<String,String> mobileMap){
		return webMobileDao.loadMobile(mobileMap.get("guid"));
	}
	
	public int saveMobile(Map<String,String> mobileMap,Map<String, Object> userMap){
		return webMobileDao.saveMobile(mobileMap, userMap);
	}
	
	public int updateMobile(Map<String,String> mobileMap){
		return webMobileDao.updateMobile(mobileMap);
	}
	
	public int updateBusable(String guid){
		return webMobileDao.updateBusable(guid);
	}
	
	public int updatePwd(String guid){
		return webMobileDao.updatePwd(guid);
	}
	
	 /**
	 * ����������д���ֵ�
	 * @param mobileMap
	 * @return
	 */
	public boolean setBankList(Map<String,String> mobileMap){
		if( !Check.IsStringNULL(mobileMap.get("bank_name")) ){
			boolean boo = true;
			for(Map<String, Object> bankList : InitServlet.bankList ){
				if( bankList.get("BANK_NAME").toString().equals(mobileMap.get("bank_name")) ){
					boo = false;
				}
			}
			if( boo ){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("BANK_NAME", mobileMap.get("bank_name"));
				if(InitServlet.bankList == null){
					InitServlet.bankList = new ArrayList<Map<String, Object>>();
				}
				InitServlet.bankList.add(map);
			}
		}
		return true;
	}
	
	public static org.json.JSONArray setJsonList(List<Map<String,Object>> list){
		org.json.JSONArray jary = new org.json.JSONArray();
		for(Map<String,Object> map : list){
			jary.put(map);
		}
		return jary;
	}
	
	public static JSONObject setJsonMap(Map<String,Object> map){
		JSONObject resultJSON = new JSONObject();
		try{
			for (Entry<String, Object> entry : map.entrySet()) {  
				resultJSON.put(entry.getKey(), entry.getValue());
			}  
		}catch (Exception e) {
			e.printStackTrace();
			Log4jUtil.log.error("Mapתjson����", e);
		}
		return resultJSON;
	}
}
