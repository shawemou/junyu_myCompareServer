package com.my.util;


import java.util.List;
import java.util.Map;

public class StringUtil {
	
	public static boolean isNotNullOrEmpty(Object obj){
		if(obj==null){
			return false;
		}
		if("".equals(obj.toString())){
			return false;
		}
		return true;
	}
	
	public static boolean isNullOrEmpty(Object obj){
		if(obj==null){
			return true;
		}
		if("".equals(obj.toString())){
			return true;
		}
		return false;
	}
	
	/**
	 * 如果obj无法正确解析，则返回false
	 * @param obj
	 * @return
	 */
	public static boolean parseBoolean(Object obj){
		return parseBoolean(obj,false);
	}
	
	/**
	 * 如果obj无法正确解析，则返回_default
	 * @param obj
	 * @return
	 */
	public static boolean parseBoolean(Object obj,boolean _default){
		try{
			return Boolean.parseBoolean(obj.toString());
		}catch(Exception e){
			return _default;
		}
	}
	
	/**
	 * 如果obj无法正确解析，则返回0
	 * @param obj
	 * @return
	 */
	public static int parseInt(Object obj){
		return parseInt(obj,0);
	}
	
	/**
	 * 如果obj无法正确解析，则返回_default
	 * @param obj
	 * @return
	 */
	public static int parseInt(Object obj,int _default){
		try{
			return Integer.parseInt(obj.toString());
		}catch(Exception e){
			return _default;
		}
	}
	
	public static String parseString(Object obj,String _default){
		try{
			return obj.toString();
		}catch(Exception e){
			return _default;
		}
	}
	
	public static String convertListToString(List<String> list,String separator){
		StringBuilder sb = new StringBuilder("");
		if(list != null && list.size() > 0){
			for(int i = 0 ; i < list.size() ; i++){
				if(list.get(i) != null && !"".equals(list.get(i))){
					sb.append("'");
					sb.append(list.get(i));
					sb.append("'");
					sb.append(separator);
				}				
			}
			if(sb.length() > 0){
				sb.deleteCharAt(sb.length() - 1);
			}
		}
		return sb.toString();
	}
	
	//把日期加入年月日
	public static String getRQ_ZH(String rq){
		if(rq != null && rq.length() >= 8){
			return rq.substring(0,4) + "年" + rq.substring(4,6) + "月" +rq.substring(6,8) + "日";
		}
		return rq;
	}
	//把日期加入年月日
	public static Object getRQ_ZH(Object rq){
		if(rq != null && rq.toString().length() >= 8){
			return rq.toString().substring(0,4) + "年" + rq.toString().substring(4,6) + "月" +rq.toString().substring(6,8) + "日";
		}
		return rq;
	}
	//把日期加入年月日 时分秒
	public static Object getRQSJ_ZH(Object rqsj){
		if(rqsj != null && rqsj.toString().length() >= 14){
			return rqsj.toString().substring(0,4) + "年" + rqsj.toString().substring(4,6) + "月" +rqsj.toString().substring(6,8) + "日"
			+ " " + rqsj.toString().substring(8,10) + ":" + rqsj.toString().substring(10,12) + ":" +rqsj.toString().substring(12,14);
		}
		return rqsj;
	}
	//小区居民 如果有附加信息加入附加信息图片
	public static String getIMGOfFJXX(Object fjxx){
		if(fjxx != null && fjxx.toString().equals("1")){
			return "<img src=\"image/fjxx.gif\">";
		}
		return "";
	}
	
	public static String getMapOfKEYtoString(Map<String,Object> map,String KEY){
		if(map == null || KEY == null){
			return "";
		}
		return map.get(KEY) == null ? "" :map.get(KEY).toString();
	}
}
