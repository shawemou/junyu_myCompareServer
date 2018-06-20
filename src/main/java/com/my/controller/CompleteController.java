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

import com.my.bean.CompareBean;
import com.my.common.CompareReturn;
import com.my.server.CompareServer;
import com.my.util.Check;
import com.my.util.CommonUtils;
import com.my.util.Log4jUtil;
import com.my.validate.CompareValidate;

@Controller
public class CompleteController {

	private static final long serialVersionUID = 2527636419937657386L;

	@Autowired
	private CompareServer compareServer;

	@RequestMapping(value = "complete", method = { RequestMethod.POST,RequestMethod.GET })
	protected void doGet(@RequestParam("version") String version, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Log4jUtil.log.warn("两照比对");
		resp.setContentType("text/html;charset=GBK");
		JSONObject resultJson = new JSONObject();
		try {
			CompareReturn cr = new CompareReturn();
			if (CommonUtils.valiVersion(req, cr, version)) {
				Map<String, String> compareMap = CommonUtils.setBeanValue(CompareBean.class, req);
				cr = CompareValidate.validate(compareMap);

				if (cr.isSuccess()) {
					cr = compareServer.compare(compareMap, cr);

					if (!compareServer.saveCompareLog(compareMap, cr)) {
						cr.setSuccess(false);
						cr.setInfo("数据保存失败");
					}
				}
			}
			resultJson.put("success", cr.isSuccess());
			resultJson.put("version", cr.isVersion());
			resultJson.put("info", cr.getInfo());
			if (!Check.IsStringNULL(cr.getCode()))
				resultJson.put("code", cr.getCode());
			if (!Check.IsStringNULL(cr.getReturn_code()))
				resultJson.put("return_code", cr.getReturn_code());
			resultJson.put("similarity", cr.getSimilarity());
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
			Log4jUtil.log.error("服务器内部错误", e);
			resp.getOutputStream().write(resultJson.toString().getBytes("GBK"));
		}
	}

}
