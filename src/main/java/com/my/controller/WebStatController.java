package com.my.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.my.bean.WebStatBean;
import com.my.server.WebStatServer;
import com.my.util.CommonUtils;
import com.my.util.Log4jUtil;

@Controller
public class WebStatController {

	private static final long serialVersionUID = -5119535832221989592L;
	
	@Autowired
	private WebStatServer webStatServer;
	
	@RequestMapping(value="webStat",method={RequestMethod.POST,RequestMethod.GET})
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Log4jUtil.log.warn("业务统计");
		request.setCharacterEncoding("GBK");
		response.setContentType("text/html;charset=GBK");

		Map<String, String> statMap = CommonUtils.setBeanValue(WebStatBean.class, request);

		Log4jUtil.log.warn("beginDate:" + statMap.get("beginDate"));
		Log4jUtil.log.warn("endDate:" + statMap.get("endDate"));
		Log4jUtil.log.warn("bank_name:" + statMap.get("bankName"));

		String method = request.getParameter("method");
		String returnJson = webStatServer.methed(method, statMap, request);
		response.getOutputStream().write(returnJson.getBytes("GBK"));
	}
}
