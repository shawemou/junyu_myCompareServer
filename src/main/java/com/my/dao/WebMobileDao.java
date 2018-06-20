package com.my.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.my.dao.base.CommonDao;
import com.my.util.AESCodec;
import com.my.util.Check;
import com.my.util.GUID;
import com.my.util.Log4jUtil;

/**
 * @ClassName: WebMobileDao 
 * @Description: web端手机用户Dao 
 *
 */
@Component
public class WebMobileDao extends CommonDao  {
	
	/**
	 * 查询列表
	 * @param mobileMap
	 * @return
	 */
	public List<Map<String, Object>> list(Map<String,String> mobileMap){
		String sql = "SELECT * FROM T_MOBILE_INFO T WHERE 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if( !Check.IsStringNULL(mobileMap.get("mobile")) ){
			sql += " AND T.MOBILE LIKE ? ";
			params.add("%" + mobileMap.get("mobile") + "%");
		}
		if( !Check.IsStringNULL(mobileMap.get("name")) ){
			sql += " AND T.NAME LIKE ? ";
			params.add("%" + mobileMap.get("name") + "%");
		}
		if( !Check.IsStringNULL(mobileMap.get("bank_name")) ){
			sql += " AND T.BANK_NAME LIKE ? ";
			params.add("%" + mobileMap.get("bank_name") + "%");
		}
		if( !Check.IsStringNULL(mobileMap.get("busable")) ){
			sql += " AND T.BUSABLE = ? ";
			params.add( mobileMap.get("busable") );
		}
		int page_count = Check.BeginIndex(mobileMap.get("page_count")) ;
		page_count = page_count == 0 ? 10 : page_count; 
		return queryReturnListMap(sql,params,"UPDATE_TIME DESC,CREATE_TIME DESC,GUID DESC", Check.BeginIndex(mobileMap.get("page_no")), page_count);
	}
	
	/**
	 * 列表行数
	 * @param mobileMap
	 * @return
	 */
	public int queryCount(Map<String,String> mobileMap){
		String sql = "SELECT COUNT(1) FROM T_MOBILE_INFO T WHERE 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if( !Check.IsStringNULL(mobileMap.get("mobile")) ){
			sql += " AND T.MOBILE LIKE ? ";
			params.add("%" + mobileMap.get("mobile") + "%");
		}
		if( !Check.IsStringNULL(mobileMap.get("name")) ){
			sql += " AND T.NAME LIKE ? ";
			params.add("%" + mobileMap.get("name") + "%");
		}
		if( !Check.IsStringNULL(mobileMap.get("bank_name")) ){
			sql += " AND T.BANK_NAME LIKE ? ";
			params.add("%" + mobileMap.get("bank_name") + "%");
		}
		if( !Check.IsStringNULL(mobileMap.get("busable")) ){
			sql += " AND T.BUSABLE = ? ";
			params.add( mobileMap.get("busable") );
		}
		Object obj = querySingleValue(sql, params);
		return Check.BeginIndex(obj.toString());
	}
	
	/**
	 * 获取单个手机
	 * @param guid
	 * @return
	 */
	public Map<String, Object> loadMobile(String guid){
		List<Object> params = new ArrayList<Object>();
		params.add(guid);
		List<Map<String,Object>> list = queryReturnListMap("SELECT * FROM T_MOBILE_INFO T WHERE GUID = ? ", params);
		return list.size() > 0 ? list.get(0) : null;
	}
	
	/**
	 * 获取银行列表
	 * @return
	 */
	public  List<Map<String, Object>> bankList(){
		String sql = "SELECT T.BANK_NAME FROM T_MOBILE_INFO T WHERE T.BANK_NAME IS NOT NULL GROUP BY T.BANK_NAME ORDER BY T.BANK_NAME";
		return queryReturnListMap(sql);
	}
	
	/**
	 * 新增
	 * @param mobileMap
	 * @param userMap
	 * @return 0失败，1成功 ，2已存在
	 */
	public int saveMobile(Map<String,String> mobileMap,Map<String, Object> userMap){
		try{
			String sql = "SELECT COUNT(1) FROM T_MOBILE_INFO T WHERE T.MOBILE = ?";
			List<Object> paramsCount = new ArrayList<Object>();
			paramsCount.add(mobileMap.get("mobile"));
			Object obj = querySingleValue(sql, paramsCount);
			if(Check.BeginIndex(obj.toString()) == 0){
				sql = "INSERT INTO T_MOBILE_INFO (GUID,MOBILE,SECRET_KEY,NAME,GENDER,ID_NUMBER,DEPARTMENT,DUTIES,DETAIL_DES,BANK_NAME,USER_GUID)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
				List<Object> params = new ArrayList<Object>();
				params.add( new GUID().toString() );
				params.add( mobileMap.get("mobile") );
				params.add( AESCodec.aesEncrypt(new GUID().toString().substring(26)) );
				params.add( mobileMap.get("name") );
				params.add( mobileMap.get("gender") );
				params.add( mobileMap.get("id_number") );
				params.add( mobileMap.get("department") );
				params.add( mobileMap.get("duties") );
				params.add( mobileMap.get("detail_des") );
				params.add( mobileMap.get("bank_name") );
				params.add( userMap.get("GUID") );
				
				return modify(sql,params) > 0 ? 1 : 0;
			}else{
				return 2;
			}
		}catch (Exception e) {
			Log4jUtil.log.error("保存手机用户信息时异常", e);
			return 0;
		}
	}
	
	/**
	 * 变更
	 * @param mobileMap
	 * @param guid
	 * @return
	 */
	public int updateMobile(Map<String,String> mobileMap){
		String sql = "SELECT COUNT(1) FROM T_MOBILE_INFO T WHERE T.GUID <> ? AND T.MOBILE = ?";
		List<Object> paramsCount = new ArrayList<Object>();
		paramsCount.add(mobileMap.get("guid"));
		paramsCount.add(mobileMap.get("mobile"));
		Object obj = querySingleValue(sql, paramsCount);
		if(Check.BeginIndex(obj.toString()) == 0){
			sql = "UPDATE T_MOBILE_INFO T SET MOBILE = ?,NAME = ?,GENDER = ?,ID_NUMBER = ?,DEPARTMENT = ?,DUTIES = ?,DETAIL_DES = ?,BANK_NAME=?,UPDATE_TIME=sysdate"
				+ " WHERE T.GUID = ?";
			List<Object> params = new ArrayList<Object>();
			params.add( mobileMap.get("mobile") );
			params.add( mobileMap.get("name") );
			params.add( mobileMap.get("gender") );
			params.add( mobileMap.get("id_number") );
			params.add( mobileMap.get("department") );
			params.add( mobileMap.get("duties") );
			params.add( mobileMap.get("detail_des") );
			params.add( mobileMap.get("bank_name") );
			params.add( mobileMap.get("guid") );
			return modify(sql,params) > 0 ? 1 : 0;
		}else{
			return 2;
		}
	}
	
	/**
	 * 状态变更
	 * @param guid
	 * @return
	 */
	public int updateBusable(String guid){
		String sql = "SELECT BUSABLE FROM T_MOBILE_INFO T WHERE T.GUID = ?";
		List<Object> paramsB = new ArrayList<Object>();
		paramsB.add( guid );
		List<Map<String, Object>> list = queryReturnListMap(sql, paramsB);
		if(list.size() > 0){
			Map<String, Object> map = list.get(0);
			String busable = map.get("BUSABLE").toString();
			sql = "UPDATE T_MOBILE_INFO T SET T.BUSABLE = ? WHERE T.GUID = ?";
			List<Object> params = new ArrayList<Object>();
			params.add( busable.equals("0") ? "1" : "0" );
			params.add( guid );
			return modify(sql,params) > 0 ? (busable.equals("0") ? 1 : 0) : -1;
		}
		return -1;
	}
	
	/**
	 * 密码重置
	 * @param guid
	 * @return
	 */
	public int updatePwd(String guid){
		String sql = "UPDATE T_MOBILE_INFO T SET T.SECRET_KEY = '"+AESCodec.aesEncrypt("111111")+"',T.SECRET_TYPE='3' WHERE T.GUID = ?";
		List<Object> params = new ArrayList<Object>();
		params.add( guid );
		return modify(sql,params);
	}
}
