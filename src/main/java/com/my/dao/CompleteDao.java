package com.my.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.my.dao.base.CommonDao;

/**
 * @ClassName: CompleteDao 
 * @Description: 获取比对成功的记录 
 *
 */
@Component
public class CompleteDao extends CommonDao {
	
	public List<Map<String, Object>> getList(Map<String,String> completeMap){
		String sql = "SELECT T.NAME,T.ID_NUMBER,T.SUCCESS CODE FROM T_COMPARE_HISTORY T WHERE T.BAR_CODE = ? ORDER BY T.CREATE_TIME ASC";
		List<Object> params = new ArrayList<Object>();
		params.add(completeMap.get("bar_code"));
		return queryReturnListMap(sql,params);
	}
}
