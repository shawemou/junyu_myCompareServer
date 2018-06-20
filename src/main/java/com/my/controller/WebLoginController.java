package com.my.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.my.common.BaseReturn;
import com.my.server.WebLoginServer;
import com.my.util.Check;
import com.my.util.Log4jUtil;
import com.my.validate.WebLoginValidate;

@Controller
public class WebLoginController {
	private static final long serialVersionUID = 470922641220769990L;
	
	@Autowired
	
	private WebLoginServer webLoginServer;

	@RequestMapping(value = "webLogin", method = { RequestMethod.POST,RequestMethod.GET })
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Log4jUtil.log.warn("��ҳ�û���¼");
		request.setCharacterEncoding("GBK");
		response.setContentType("text/html;charset=GBK");

		String login_name = Check.HadStr(request.getParameter("login_name"));
		String login_psd = Check.HadStr(request.getParameter("login_psd"));
		//1,У������������Ƿ�Ϲ�(��̨�ı�У��)
		BaseReturn br = WebLoginValidate.validate(login_name, login_psd);
		if (br.isSuccess()) {
			//2,У���¼�������������ݿ��е��û����������Ƿ�ƥ��
			br = webLoginServer.login(login_name, login_psd, br, request);
		}

		JSONObject resultJson = new JSONObject();
		try {
			resultJson.put("success", br.isSuccess());
			resultJson.put("info", br.getInfo());
			response.getOutputStream().write(resultJson.toString().getBytes("GBK"));
		} catch (JSONException e) {
			response.getOutputStream().write("��¼�쳣".getBytes("GBK"));
			Log4jUtil.log.error("��¼�쳣", e);
		}
	}
}
