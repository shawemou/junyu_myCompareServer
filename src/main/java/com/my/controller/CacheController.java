package com.my.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.bean.CompareBean;
import com.my.common.CompareReturn;
import com.my.server.CacheServer;
import com.my.server.CompareServer;
import com.my.util.Check;
import com.my.util.CommonUtils;
import com.my.util.Log4jUtil;
import com.my.util.ShaweCommonUtils;
import com.my.validate.CompareValidate;

@Controller
public class CacheController {
	private static final long serialVersionUID = -7203707929907939318L;
	
	@Autowired
	private CacheServer cacheServer;
	
	@Autowired
	private  CompareServer compareServer;
	
	@RequestMapping(value="cache",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	protected Map<String,Object> doGet(
			@RequestParam("version")String version,
			CompareBean compareBean,
			HttpServletRequest req, 
			HttpServletResponse resp) throws ServletException, IOException {
		
		Log4jUtil.log.warn("暂存两照比对");
		
		HashMap<String, Object> resultMap = new HashMap<String,Object>();
		try{
			CompareReturn cr = new CompareReturn();
			if(ShaweCommonUtils.valiVersion(cr,version)){
				//1, 手机号   二维码  name idnumber  id_type
				Map<String,String> compareMap = CommonUtils.setBeanValue(CompareBean.class,req);
				cr = CompareValidate.validate(compareMap);
				if( cr.isSuccess() ){
					cr = compareServer.compare(compareMap, cr);
					if(!cacheServer.saveCacheLog(compareMap, cr)){
						cr.setSuccess(false);
						cr.setInfo("数据保存失败");
					}
				}
			}
			
			resultMap.put("success", cr.isSuccess());
			resultMap.put("version", cr.isVersion());
			resultMap.put("info", cr.getInfo());
			if(!Check.IsStringNULL(cr.getCode()))
				resultMap.put("code", cr.getCode());
			if(!Check.IsStringNULL(cr.getReturn_code()))
				resultMap.put("return_code", cr.getReturn_code());
			resultMap.put("similarity", cr.getSimilarity());
			Log4jUtil.log.warn(resultMap.toString());
			
		}catch (Exception e) {
			try {
				resultMap.put("success", false);
				resultMap.put("version", true);
				resultMap.put("info", "服务器内部错误");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			Log4jUtil.log.error("服务器内部错误", e);
		}
		return resultMap;
	}
}