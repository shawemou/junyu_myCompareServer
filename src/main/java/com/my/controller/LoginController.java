package com.my.controller;

import java.io.IOException;
import java.net.URLDecoder;
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

import com.my.bean.LoginBean;
import com.my.common.BaseReturn;
import com.my.common.ReadSetting;
import com.my.server.MobileServer;
import com.my.util.Log4jUtil;
import com.my.util.ShaweCommonUtils;

@Controller
public class LoginController {

	@Autowired
	private MobileServer mobileServer;

	private static final long serialVersionUID = -6721472665784197425L;

	@RequestMapping(value = "login", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	protected Map<String, Object> login(
			@RequestParam("mobile") String mobile, 
			@RequestParam("version") String version,
			@RequestParam("secret_key") String secret_key, 
			LoginBean loginBean) throws ServletException, IOException {
		
		Log4jUtil.log.warn("当前版本:" + ReadSetting.getInstance().getVersion());
		
		loginBean.setSecret_key(URLDecoder.decode(loginBean.getSecret_key(), "UTF-8"));
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		try {
			BaseReturn br = new BaseReturn();
			// 1,校验版本是否为空;判断版本号是否和内置的版本号一致
			if (ShaweCommonUtils.valiVersion(br, version)) {
				// 2,校验手机号和密码
				br = ShaweCommonUtils.validate(loginBean);
				if (br.isSuccess()) {
					// 3,返回的br中含有是否success和info,校验密码和手机号
					br = mobileServer.loginMobile(loginBean, br);
				}
				boolean flag = mobileServer.saveLoginMobileLog(loginBean, br);
			}
			returnMap.put("success", br.isSuccess());
			returnMap.put("version", br.isVersion());
			returnMap.put("info", br.getInfo());
			Log4jUtil.log.warn(returnMap.toString());
		} catch (Exception e) {
			try {
				returnMap.put("success", false);
				returnMap.put("version", true);
				returnMap.put("info", "服务器内部错误");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			Log4jUtil.log.error("登录异常", e);
		}
		return returnMap;
	}
}
