package com.my.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.bean.CacheListBean;
import com.my.common.CompareReturn;
import com.my.server.CacheServer;
import com.my.util.Check;
import com.my.util.CommonUtils;
import com.my.util.Log4jUtil;

@Controller
public class CacheListController {
	
	private static final long serialVersionUID = -7203707929907939318L;
	
	@Autowired
	private CacheServer cacheServer;
	
	@RequestMapping(value="cacheList",method={RequestMethod.POST,RequestMethod.GET})
	protected void doGet(@RequestParam("version")String version,HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Log4jUtil.log.warn("�������ձȶ�����");
		resp.setContentType("text/html;charset=GBK");
		Map<String,String> cacheListMap = CommonUtils.setBeanValue(CacheListBean.class,req);
		CompareReturn cr = new CompareReturn();
		if(CommonUtils.valiVersion(req,cr,version)){
			String json = "{\"success\":false,\"info\":\"���ӵ�ַ������\"}";
			if(!Check.IsStringNULL(cacheListMap.get("method")) ){
				String method = cacheListMap.get("method");
				if(method.equals("list")){
					Log4jUtil.log.warn("��ȡ�б�");
					json = list(cr, cacheListMap).toString();
				}else if(method.equals("delete")){
					Log4jUtil.log.warn("ɾ���ݴ�");
					json = delete(cr, cacheListMap).toString();
				}else if(method.equals("done")){
					Log4jUtil.log.warn("�ݴ�ת��ʽ");
					json = done(cr, cacheListMap).toString();
				}
			}
			Log4jUtil.log.warn(json);
			
			resp.getOutputStream().write(json.getBytes("GBK"));
		}else{
			JSONObject resultJson = new JSONObject();
			try{
				resultJson.put("success", cr.isSuccess());
				resultJson.put("version", cr.isVersion());
				resultJson.put("info", cr.getInfo());
				Log4jUtil.log.warn(resultJson.toString());
				resp.getOutputStream().write(resultJson.toString().getBytes("GBK"));
			}catch (Exception e) {
				Log4jUtil.log.error("�������ձȶ������쳣",e);
			}
		}
	}
	
	/**
	 * ��ѯ��������
	 * @param cr
	 * @param cacheListMap
	 * @return
	 */
	private JSONObject list(CompareReturn cr, Map<String,String> cacheListMap){
		JSONObject resultJson = new JSONObject();
		try{
			if( Check.IsStringNULL( cacheListMap.get("bar_code") ) ){
				cr.setSuccess(false);
				cr.setInfo("�����벻��Ϊ��");
			}else{
				org.json.JSONArray json = cacheServer.getList(cacheListMap);
				resultJson.put("data", json);
			}
			resultJson.put("success", cr.isSuccess());
			resultJson.put("version", cr.isVersion());
			resultJson.put("info", cr.getInfo());
			return resultJson;
		}catch (Exception e) {
			try {
				resultJson.put("success", false);
				resultJson.put("version", true);
				resultJson.put("info", "�������ڲ�����");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
	        Log4jUtil.log.error("��ȡ�ݴ����ձȶ������쳣", e);
		}
		return resultJson;
	}
	
	/**
	 * ɾ����������
	 * @param cr
	 * @param cacheListMap
	 * @return
	 */
	private JSONObject delete(CompareReturn cr, Map<String,String> cacheListMap){
		JSONObject resultJson = new JSONObject();
		try{
			if( Check.IsStringNULL( cacheListMap.get("data") ) ){
				cr.setSuccess(false);
				cr.setInfo("��ѡ����Ҫɾ��������");
			}else{
				if(cacheServer.delete(cacheListMap.get("data"))){
					cr.setSuccess(true);
					cr.setInfo("�ɹ�");
				}else{
					cr.setSuccess(false);
					cr.setInfo("ɾ����������ʧ��");
				}
			}
			resultJson.put("success", cr.isSuccess());
			resultJson.put("version", cr.isVersion());
			resultJson.put("info", cr.getInfo());
			return resultJson;
		}catch (Exception e) {
			try {
				resultJson.put("success", false);
				resultJson.put("version", true);
				resultJson.put("info", "�������ڲ�����");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
	        Log4jUtil.log.error("ɾ����������ʧ���쳣", e);
		}
		return resultJson;
	}
	
	/**
	 * ����ת��ʽ
	 * @param cr
	 * @param cacheListMap
	 * @return
	 */
	private JSONObject done(CompareReturn cr, Map<String,String> cacheListMap){
		JSONObject resultJson = new JSONObject();
		try{
			if( Check.IsStringNULL( cacheListMap.get("bar_code") ) ){
				cr.setSuccess(false);
				cr.setInfo("�����벻��Ϊ��");
			}else{
				if(cacheServer.done(cacheListMap.get("bar_code"))){
					cr.setSuccess(true);
					cr.setInfo("�ɹ�");
				}else{
					cr.setSuccess(false);
					cr.setInfo("������������ʽ��ʧ��");
				}
			}
			resultJson.put("success", cr.isSuccess());
			resultJson.put("version", cr.isVersion());
			resultJson.put("info", cr.getInfo());
			return resultJson;
		}catch (Exception e) {
			try {
				resultJson.put("success", false);
				resultJson.put("version", true);
				resultJson.put("info", "�������ڲ�����");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
	        Log4jUtil.log.error("������������ʽ���쳣", e);
		}
		return resultJson;
	}

}
