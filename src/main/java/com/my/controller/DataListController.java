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

import com.my.bean.CompleteBean;
import com.my.common.BaseReturn;
import com.my.server.DataListServer;
import com.my.util.CommonUtils;
import com.my.util.Log4jUtil;
import com.my.validate.CompleteValidate;

@Controller
public class DataListController {

	private static final long serialVersionUID = 6263578137449470289L;

	@Autowired
	private DataListServer dataListServer;

	@RequestMapping(value = "dataList", method = { RequestMethod.POST,RequestMethod.GET })
	protected void dataList(@RequestParam("version") String version, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Log4jUtil.log.warn("业务详情");
		Log4jUtil.log.warn(req.getParameter("mobile"));
		resp.setContentType("text/html;charset=GBK");
		JSONObject resultJson = new JSONObject();
		try {
			BaseReturn br = new BaseReturn();
			br.setInfo("成功");
			if (CommonUtils.valiVersion(req, br, version)) {
				// 1,相当于返回一个CompleteBean,一个字段bar_code(条形码)
				Map<String, String> completeMap = CommonUtils.setBeanValue(CompleteBean.class, req);
				// 2,条形码不能为空
				br = CompleteValidate.validate(completeMap);

				if (br.isSuccess()) {
					// 3,查询两个表T_COMPARE_HISTORY 和 T_CACHE_HISTORY
					org.json.JSONArray json = dataListServer.getList(completeMap);
					if (json != null) {
						resultJson.put("data", json);
					} else {
						br.setSuccess(false);
						br.setInfo("获取业务详情失败");
					}
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
				resultJson.put("version", true);
				resultJson.put("info", "服务器内部错误");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			Log4jUtil.log.error("获取业务详情数据异常", e);
			resp.getOutputStream().write(resultJson.toString().getBytes("GBK"));
		}
	}
}
