package com.my.server;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.common.BaseReturn;
import com.my.dao.WebLoginDao;
import com.my.util.AESCodec;

@Service
public class WebLoginServer {
	
	@Autowired
	private WebLoginDao webLoginDao;

	public BaseReturn login(String login_name, String login_psd,BaseReturn br, HttpServletRequest request){
		List<Map<String, Object>> list =webLoginDao.login(login_name);
		if(list == null || list.size() <= 0){
			br.setSuccess(false);
			br.setInfo("登录失败,登录名或密码不存在");
			return br;
		}else{
			Map<String, Object> map = list.get(0);
			if(map.get("BUSABLE") != null && map.get("BUSABLE").toString().equals("0")){
				br.setSuccess(false);
				br.setInfo("此用户已停用");
				return br;
			}

			if(map.get("LOGIN_PWD") == null || !map.get("LOGIN_PWD").toString().equals(AESCodec.aesEncrypt(login_psd))){
				br.setSuccess(false);
				br.setInfo("密码验证不通过");
				return br;
			}
			request.getSession().setAttribute("user", map);
		}
		
		br.setSuccess(true);
		br.setInfo("登录成功");
		return br;
	}
}
