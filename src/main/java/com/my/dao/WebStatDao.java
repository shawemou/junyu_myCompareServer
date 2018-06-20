package com.my.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.my.dao.base.CommonDao;
import com.my.util.Check;

/**
 * @ClassName: WebStatDao 
 * @Description: 统计一段时间内的案件数 网页显示
 * @author lulinlin
 * @date 2017-2-14 下午04:33:30 
 *
 */
@Component
public class WebStatDao extends CommonDao {

	public List<Map<String, Object>> list(Map<String,String> map){
		String sql = "SELECT B.BANK_NAME,B.NAME,A.MOBILE,A.CC FROM (SELECT T.MOBILE,COUNT(1) CC FROM T_COMPARE_HISTORY T WHERE 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if( !Check.IsStringNULL(map.get("beginDate")) ){
			sql += " AND T.CREATE_TIME >= to_date(?,'yyyy-mm-dd') ";
			params.add( map.get("beginDate") );
		}
		if( !Check.IsStringNULL(map.get("endDate")) ){
			sql += " AND T.CREATE_TIME <= to_date(?,'yyyy-mm-dd')  ";
			params.add( map.get("endDate") );
		}
		sql += "GROUP BY T.MOBILE) A "
			+ "left join (SELECT M.BANK_NAME,M.NAME,M.MOBILE FROM T_MOBILE_INFO M)B ON A.MOBILE = B.MOBILE ";
		
		if( !Check.IsStringNULL(map.get("bankName")) ){
			sql += " WHERE BANK_NAME LIKE ? ";
			params.add("%" + map.get("bankName") + "%");
		}

		int page_count = Check.BeginIndex(map.get("page_count")) ;
		page_count = page_count == 0 ? 10 : page_count; 
		return queryReturnListMap(sql,params,"BANK_NAME,CC DESC,MOBILE", Check.BeginIndex(map.get("page_no")), page_count);
	}
	
	/**
	 * 列表行数
	 * @param mobileMap
	 * @return
	 */
	public int queryCount(Map<String,String> map){
		String sql = "SELECT COUNT(1) FROM (SELECT T.MOBILE FROM T_COMPARE_HISTORY T WHERE 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if( !Check.IsStringNULL(map.get("beginDate")) ){
			sql += " AND T.CREATE_TIME >= to_date(?,'yyyy-mm-dd') ";
			params.add( map.get("beginDate") );
		}
		if( !Check.IsStringNULL(map.get("endDate")) ){
			sql += " AND T.CREATE_TIME <= to_date(?,'yyyy-mm-dd')  ";
			params.add( map.get("endDate") );
		}
		
		sql += "GROUP BY T.MOBILE) A "
			+ "LEFT JOIN (SELECT M.BANK_NAME,M.MOBILE FROM T_MOBILE_INFO M)B ON A.MOBILE = B.MOBILE";
		if( !Check.IsStringNULL(map.get("bankName")) ){
			sql += " WHERE BANK_NAME LIKE ? ";
			params.add("%" + map.get("bankName") + "%");
		}
		Object obj = querySingleValue(sql, params);
		return Check.BeginIndex(obj.toString());
	}
}
