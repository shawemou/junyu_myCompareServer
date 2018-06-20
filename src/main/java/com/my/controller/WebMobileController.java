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

import com.my.bean.WebMobileBean;
import com.my.server.WebMobileServer;
import com.my.util.CommonUtils;
import com.my.util.Log4jUtil;

@Controller
public class WebMobileController {
	
	private static final long serialVersionUID = -5119535832221989592L;
	
	@Autowired
	private WebMobileServer webMobileServer;

	@RequestMapping(value = "webMobile", method = { RequestMethod.POST,RequestMethod.GET })
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Log4jUtil.log.warn("手机用户维护");
		request.setCharacterEncoding("GBK");
		response.setContentType("text/html;charset=GBK");

		Map<String, String> mobileMap = CommonUtils.setBeanValue(WebMobileBean.class, request);
		String method = request.getParameter("method");
		String returnJson = webMobileServer.methed(method, mobileMap, request);
		response.getOutputStream().write(returnJson.getBytes("GBK"));
	}
}
