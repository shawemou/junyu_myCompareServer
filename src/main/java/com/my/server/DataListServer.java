package com.my.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.dao.CacheDao;
import com.my.dao.CompleteDao;

@Service
public class DataListServer extends BaseServer  {
	
	@Autowired
	private CacheDao cacheDao;
	
	@Autowired
	private CompleteDao completeDao;
	
	public org.json.JSONArray getList(Map<String,String> completeMap){
		//1,根据条形码在T_COMPARE_HISTORY 查询 公司名字  公司number  是否成功
		List<Map<String, Object>> completeList = completeDao.getList(completeMap);
		
		//2,根据条形码在T_CACHE_HISTORY 查询  GUID,NAME,ID_NUMBER,SUCCESS CODE,SOURCE_INFO
		List<Map<String, Object>> cacheList = cacheDao.getList(completeMap);
		
		List<Map<String, Object>> list_sce = new ArrayList<Map<String, Object>>();
		if( completeList != null){
			Map<String,Object> map_tmep = new HashMap<String,Object>();
			//遍历第一个list
			
			for( Map<String,Object> map : completeList ){
				if( !map_tmep.containsKey(map.get("NAME").toString() + "||" + map.get("ID_NUMBER").toString() + "||" + map.get("CODE").toString() ) ){
					map_tmep.put(map.get("NAME").toString() + "||" + map.get("ID_NUMBER").toString() + "||" + map.get("CODE").toString(), null);
					map.put("ETYPE", "1");
					list_sce.add(map);
				}
			}
			
		}
		if( cacheList != null){
			//遍历第二个list
			Map<String,Object> map_tmep = new HashMap<String,Object>();
			for( Map<String,Object> map : cacheList ){
				if( !map_tmep.containsKey(map.get("NAME").toString() + "||" + map.get("ID_NUMBER").toString() + "||" + map.get("CODE").toString() ) ){
					map_tmep.put(map.get("NAME").toString() + "||" + map.get("ID_NUMBER").toString() + "||" + map.get("CODE").toString(), null);
					map.put("ETYPE", "0");
					list_sce.add(map);
				}
			}
			
		}
		return setJsonList(list_sce);
	}
	
}
