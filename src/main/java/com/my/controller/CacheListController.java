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
		Log4jUtil.log.warn("操作两照比对数据");
		resp.setContentType("text/html;charset=GBK");
		Map<String,String> cacheListMap = CommonUtils.setBeanValue(CacheListBean.class,req);
		CompareReturn cr = new CompareReturn();
		if(CommonUtils.valiVersion(req,cr,version)){
			String json = "{\"success\":false,\"info\":\"链接地址不存在\"}";
			if(!Check.IsStringNULL(cacheListMap.get("method")) ){
				String method = cacheListMap.get("method");
				if(method.equals("list")){
					Log4jUtil.log.warn("获取列表");
					json = list(cr, cacheListMap).toString();
				}else if(method.equals("delete")){
					Log4jUtil.log.warn("删除暂存");
					json = delete(cr, cacheListMap).toString();
				}else if(method.equals("done")){
					Log4jUtil.log.warn("暂存转正式");
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
				Log4jUtil.log.error("操作两照比对数据异常",e);
			}
		}
	}
	
	/**
	 * 查询缓存数据
	 * @param cr
	 * @param cacheListMap
	 * @return
	 */
	private JSONObject list(CompareReturn cr, Map<String,String> cacheListMap){
		JSONObject resultJson = new JSONObject();
		try{
			if( Check.IsStringNULL( cacheListMap.get("bar_code") ) ){
				cr.setSuccess(false);
				cr.setInfo("条形码不能为空");
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
				resultJson.put("info", "服务器内部错误");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
	        Log4jUtil.log.error("获取暂存两照比对数据异常", e);
		}
		return resultJson;
	}
	
	/**
	 * 删除缓存数据
	 * @param cr
	 * @param cacheListMap
	 * @return
	 */
	private JSONObject delete(CompareReturn cr, Map<String,String> cacheListMap){
		JSONObject resultJson = new JSONObject();
		try{
			if( Check.IsStringNULL( cacheListMap.get("data") ) ){
				cr.setSuccess(false);
				cr.setInfo("请选择需要删除的数据");
			}else{
				if(cacheServer.delete(cacheListMap.get("data"))){
					cr.setSuccess(true);
					cr.setInfo("成功");
				}else{
					cr.setSuccess(false);
					cr.setInfo("删除缓存数据失败");
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
				resultJson.put("info", "服务器内部错误");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
	        Log4jUtil.log.error("删除缓存数据失败异常", e);
		}
		return resultJson;
	}
	
	/**
	 * 缓存转正式
	 * @param cr
	 * @param cacheListMap
	 * @return
	 */
	private JSONObject done(CompareReturn cr, Map<String,String> cacheListMap){
		JSONObject resultJson = new JSONObject();
		try{
			if( Check.IsStringNULL( cacheListMap.get("bar_code") ) ){
				cr.setSuccess(false);
				cr.setInfo("条形码不能为空");
			}else{
				if(cacheServer.done(cacheListMap.get("bar_code"))){
					cr.setSuccess(true);
					cr.setInfo("成功");
				}else{
					cr.setSuccess(false);
					cr.setInfo("缓存数据入正式库失败");
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
				resultJson.put("info", "服务器内部错误");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
	        Log4jUtil.log.error("缓存数据入正式库异常", e);
		}
		return resultJson;
	}

}
