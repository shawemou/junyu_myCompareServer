package com.my.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.common.CompareReturn;
import com.my.dao.CacheDao;

@Service
public class CacheServer extends BaseServer {
	@Autowired
	private CacheDao cachDao;
	
	public boolean saveCacheLog(Map<String,String> compareMap, CompareReturn cr){
		return new CacheDao().saveCacheLog(compareMap, cr);
	}
	
	/**
	 * 获取暂存列表
	 * @param completeMap
	 * @return
	 */
	public org.json.JSONArray getList(Map<String,String> bar_code){
		List<Map<String, Object>> list = new CacheDao().getList(bar_code);
		if( list != null){
			return setJsonList(list);
		}
		return null;
	}
	
	/**
	 * 删除缓存数据
	 * @param data
	 * @return
	 */
	public boolean delete(String data){
		try {
			JSONArray json = new JSONArray(data);
			List<String> list = new ArrayList<String>();
			for(int i = 0; i < json.length(); i++){
				list.add(json.getString(i));
			}
			if(list.size() > 0 ){
				return new CacheDao().delete(list) > 0;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 暂存数据转正式
	 * @param data
	 * @return
	 */
	public boolean done(String bar_code){
		return new CacheDao().done(bar_code);
	}
	
	public static void main(String[] args) {
			JSONArray json = new JSONArray();
			json.put("1111");
			json.put("1111");
			json.put("1111");
			System.out.println(json.toString());
	}
}
