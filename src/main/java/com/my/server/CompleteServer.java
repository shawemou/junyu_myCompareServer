package com.my.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.my.dao.CompleteDao;

public class CompleteServer extends BaseServer {

	public org.json.JSONArray getList(Map<String,String> completeMap){
		List<Map<String, Object>> list = new CompleteDao().getList(completeMap);
		if( list != null){
			Map<String,Object> map_tmep = new HashMap<String,Object>();
			List<Map<String, Object>> list_sce = new ArrayList<Map<String, Object>>();
			for( Map<String,Object> map : list ){
				if( !map_tmep.containsKey(map.get("NAME").toString() + "||" + map.get("ID_NUMBER").toString()) ){
					map_tmep.put(map.get("NAME").toString() + "||" + map.get("ID_NUMBER").toString(), null);
					list_sce.add(map);
				}
			}
			return setJsonList(list_sce);
		}
		return null;
	}
	
}
