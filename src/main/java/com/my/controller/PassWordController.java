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

import com.my.bean.PasswordBean;
import com.my.common.BaseReturn;
import com.my.server.MobileServer;
import com.my.util.CommonUtils;
import com.my.util.Log4jUtil;
import com.my.validate.PasswordValidate;

@Controller
public class PassWordController {

	private static final long serialVersionUID = 370146623969238221L;
	
	@Autowired
	private MobileServer mobileServer;

	@RequestMapping(value = "pwdModify", method = { RequestMethod.POST,RequestMethod.GET })
	protected void changePassWord(@RequestParam("version")String version,HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Log4jUtil.log.warn("–ﬁ∏ƒ√‹¬Î");
		resp.setContentType("text/html;charset=GBK");
		JSONObject resultJson = new JSONObject();
		try {
			BaseReturn br = new BaseReturn();
			if (CommonUtils.valiVersion(req, br,version)) {
				Map<String, String> passwordMap = CommonUtils.setBeanValue(PasswordBean.class, req);
				br = PasswordValidate.validate(passwordMap);

				if (br.isSuccess()) {
					mobileServer.passwordModify(passwordMap, br);
				}
			}

			resultJson.put("success", br.isSuccess());
			resultJson.put("version", br.isVersion());
			resultJson.put("info", br.getInfo());
			Log4jUtil.log.warn(resultJson.toString());

			resp.getOutputStream().write(resultJson.toString().getBytes("GBK"));
		} catch (Exception e) {
			try {
				resultJson.put("success", false);
				resultJson.put("version", false);
				resultJson.put("info", "–ﬁ∏ƒ√‹¬Î“Ï≥£");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			Log4jUtil.log.error("–ﬁ∏ƒ√‹¬Î“Ï≥£", e);
			resp.getOutputStream().write(resultJson.toString().getBytes("GBK"));
		}
	}

}
