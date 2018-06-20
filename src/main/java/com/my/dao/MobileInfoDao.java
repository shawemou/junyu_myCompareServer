package com.my.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.my.common.BaseReturn;
import com.my.dao.base.CommonDao;
import com.my.util.AESCodec;
import com.my.util.GUID;

/**
 * @ClassName: MobileInfoDao 
 * @Description: 手机用户
 *
 */
@Component
public class MobileInfoDao extends CommonDao {

	public List<Map<String, Object>> loginMobile(Map<String,String> loginMap){
		String sql = "SELECT SECRET_KEY,BUSABLE FROM T_MOBILE_INFO T WHERE T.MOBILE = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(loginMap.get("mobile"));
		return queryReturnListMap(sql,params);
	}
	
	public boolean saveLoginMobileLog(Map<String,String> loginMap, BaseReturn br){
		String mobile = loginMap.get("mobile");
		if(mobile != null && mobile.length() > 11){
			mobile = mobile.substring(0, 11);
		}
		String preparedSql = "INSERT INTO T_MOBILE_LOGIN_LOG (GUID,MOBILE,SECRET_KEY,SUCCESS) VALUES (?,?,?,?)";
		List<Object> params = new ArrayList<Object>();
		params.add(new GUID().toString());
		params.add(mobile );
		params.add(loginMap.get("secret_key"));
		params.add(br.isSuccess() ? "1" : "0");
		return modify(preparedSql, params) > 0;
	}
	
	public boolean passwordModify(Map<String,String> passwordMap, BaseReturn br){
		String preparedSql = "UPDATE T_MOBILE_INFO T SET T.SECRET_KEY = ?,T.SECRET_TYPE='2' WHERE T.MOBILE = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(AESCodec.aesEncrypt(passwordMap.get("new_secret_key")));
		params.add(passwordMap.get("mobile"));
		return modify(preparedSql, params) > 0;
	}
}
