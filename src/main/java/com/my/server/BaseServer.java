package com.my.server;

import java.util.List;
import java.util.Map;

public class BaseServer {
	public static org.json.JSONArray setJsonList(List<Map<String,Object>> list){
		org.json.JSONArray jary = new org.json.JSONArray();
		for(Map<String,Object> map : list){
			jary.put(map);
		}
		return jary;
	}
}
