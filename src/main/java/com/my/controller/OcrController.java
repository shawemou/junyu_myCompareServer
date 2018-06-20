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

import com.my.bean.OcrBean;
import com.my.common.OcrReturn;
import com.my.server.OcrServer;
import com.my.util.Check;
import com.my.util.CommonUtils;
import com.my.util.Log4jUtil;
import com.my.validate.OcrValidate;

@Controller
public class OcrController {

	private static final long serialVersionUID = 7598178559826819168L;
	
	@Autowired
	private OcrServer orcServer;

	@RequestMapping(value = "ocr", method = { RequestMethod.POST,RequestMethod.GET })
	protected void ocr(@RequestParam("version")String version,HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Log4jUtil.log.warn("OCR识别开始");
		resp.setContentType("text/html;charset=GBK");
		JSONObject resultJson = new JSONObject();
		try {
			//1,name,id_number,begainData
			OcrReturn or = new OcrReturn();
			if (CommonUtils.valiVersion(req, or,version)) {
				//2,OrcBean 含有 bar_code , id_phone , mobile 
				Map<String, String> ocrMap = CommonUtils.setBeanValue(OcrBean.class, req);
				//3,验证手机号,照片,扫的条形码 (这几个字段是否为空)
				or = OcrValidate.validate(ocrMap);
				if (or.isSuccess()) {
					or = orcServer.ocr(ocrMap, or);
					orcServer.saveOCRLog(ocrMap, or);
				}
			}
			resultJson.put("success", or.isSuccess());
			resultJson.put("version", or.isVersion());
			resultJson.put("info", or.getInfo());
			if (!Check.IsStringNULL(or.getName()))
				resultJson.put("name", or.getName());
			if (!Check.IsStringNULL(or.getId_number()))
				resultJson.put("id_number", or.getId_number());

			Log4jUtil.log.warn(resultJson.toString());
			resp.getOutputStream().write(resultJson.toString().getBytes("GBK"));
		} catch (Exception e) {
			try {
				resultJson.put("success", false);
				resultJson.put("version", true);
				resultJson.put("info", "服务器内部错误");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			Log4jUtil.log.error("身份证OCR识别异常", e);
			resp.getOutputStream().write(resultJson.toString().getBytes("GBK"));
		}
	}
}
