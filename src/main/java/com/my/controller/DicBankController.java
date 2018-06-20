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

import com.my.bean.DicBankBean;
import com.my.server.DicBankServer;
import com.my.util.CommonUtils;
import com.my.util.Log4jUtil;

@Controller
public class DicBankController {

	private static final long serialVersionUID = -5119535832221989592L;

	@Autowired
	private DicBankServer dicBanServer;

	@RequestMapping(value = "dicBank", method = { RequestMethod.POST,RequestMethod.GET })
	public void getBankMap(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Log4jUtil.log.warn("×Öµä·þÎñ");
		request.setCharacterEncoding("GBK");
		response.setContentType("text/html;charset=GBK");

		Map<String, String> bankMap = CommonUtils.setBeanValue(DicBankBean.class, request);
		String returnJson = dicBanServer.getDicBank(bankMap);
		response.getOutputStream().write(returnJson.getBytes("GBK"));
	}
}
