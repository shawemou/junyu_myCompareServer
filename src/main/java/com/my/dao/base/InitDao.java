package com.my.dao.base;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.my.util.Check;
import com.my.util.StringUtil;


@Component
public class InitDao extends CommonDao {

	public int queryUserTotal(String name){
		List<Object> params = new ArrayList<Object>();
		params.add(name);
		Object obj = querySingleValue("SELECT COUNT(1) FROM WATERMARKFACEUSER T WHERE T.USERNAME = ?",params);
		return StringUtil.parseInt(obj,0);
	}


	public Map<String, Object> getRecordList(String name,String beginTime, String endTime){
		List<Object> params = new ArrayList<Object>();
		params.add(name);
//		params.add(beginTime);
//		params.add(endTime);
		
		String sql = "select t.name,t.result,count(1) CON from watermarkfaceverifyrecord t "
			+ " where t.sendtime >= to_date('"+beginTime+"','yyyy-MM-dd HH24:mi:ss') "
			+ " and t.sendtime <= to_date('"+endTime+"','yyyy-MM-dd HH24:mi:ss') "
			+ " and t.username = ? "
			+ " group by t.name,t.result order by t.name,t.result";
		
		List<Map<String, Object>> list = queryReturnListMap(sql,params);
		int test0Conn = 0;
		int test0ErrConn = 0;
		int test1Conn = 0;
		int test1SuccConn = 0;
		for(Map<String, Object> map : list){
			if(map.get("NAME") != null && map.get("RESULT") != null){
				if(map.get("NAME").toString().equals("test0")){
					test0Conn += StringUtil.parseInt(map.get("CON"),0);
					if(map.get("RESULT").toString().equals("0") || map.get("RESULT").toString().equals("-3")){
						test0ErrConn += StringUtil.parseInt(map.get("CON"),0);
					}
				}else if(map.get("NAME").toString().equals("test1")){
					test1Conn += StringUtil.parseInt(map.get("CON"),0);
					if(map.get("RESULT").toString().equals("0") || map.get("RESULT").toString().equals("-3")){
						test1SuccConn += StringUtil.parseInt(map.get("CON"),0);
					}
				}
			}
		}
		
		String sql2 = "select count(1) CON, "
			+ " round(sum( (to_date(to_char(processtime,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')- "
			+ " to_date(to_char(recvtime,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss'))*24*60*60 )/count(1),2) avg, "
			+ " max((to_date(to_char(processtime,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')- "
			+ " to_date(to_char(recvtime,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss'))*24*60*60) maxTime, "
			+ " min((to_date(to_char(processtime,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')- "
			+ " to_date(to_char(recvtime,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss'))*24*60*60) minTime "
			+ " from watermarkfaceverifyrecord t  "
			+ " where t.sendtime >= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') "
			+ " and t.sendtime <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') "
			+ " and t.processtime is not null "
			+ " and t.recvtime is not null "
			+ " and t.username = ? ";
		
		List<Map<String, Object>> list2 = queryReturnListMap(sql2,params);
		
		Map<String, Object> map = new HashMap<String, Object>();
		float far = (float)test0ErrConn*100/test0Conn;
		float passrate = (float)test1SuccConn*100/test1Conn;
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
		if(((Float)far).isNaN()){
			map.put("FAR", "0.00% ("+test0ErrConn+"/"+test0Conn+")");
		}else{
			map.put("FAR", df.format(far) + "% ("+test0ErrConn+"/"+test0Conn+")");
		}
		if(((Float)passrate).isNaN()){
			map.put("PassRate", "0.00% ("+test1SuccConn+"/"+test1Conn+")");
		}else{
			map.put("PassRate", df.format(passrate) + "% ("+test1SuccConn+"/"+test1Conn+")");
		}
		if(list2 != null && list2.size() > 0){
			Map<String, Object> map2 = list2.get(0);
			map.put("avg", map2.get("AVG"));
			map.put("maxTime", map2.get("MAXTIME"));
			map.put("minTime", map2.get("MINTIME"));
			map.put("conn", map2.get("CON"));
		}
		return map;
	}
	
	public List<Map<String, Object>> getRecordList(String name,String beginTime, String endTime, String result, String issucc, int pageNo, int pageSize){
		List<Object> params = new ArrayList<Object>();
		params.add(name);
//		params.add(beginTime);
//		params.add(endTime);
		
		String preparedSql = "select guid,name,to_char(RECVTIME,'yyyy-mm-dd hh24:mi:ss') RECVTIME,"
			+ " to_char(PROCESSTIME,'yyyy-mm-dd hh24:mi:ss') PROCESSTIME,RESULT "
			+ " from watermarkfaceverifyrecord t"
			+ " where t.sendtime >= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') "
			+ " and t.sendtime <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') "
			+ " and t.processtime is not null "
			+ " and t.recvtime is not null "
			+ " and t.username = ? ";
		
		if(!Check.IsStringNULL(result)){
			preparedSql += " and t.result in(?,?)";
			if(result.equals("0"))
			{
				params.add(result);
				params.add("-3");
			}else{
				params.add("1");
				params.add("-4");
			}
		}
		
		if(!Check.IsStringNULL(issucc)){
			if(issucc.equals("1")){
				preparedSql += " and ((t.name='test0' and t.result in (1,-4)) or (t.name='test1' and t.result in (0,-3)))";
			}else{
				preparedSql += " and ((t.name='test0' and t.result in (0,-3)) or (t.name='test1' and t.result in (1,-4)))";
			}
		}
		
		List<Map<String, Object>> list = queryReturnListMap(preparedSql, params, " PROCESSTIME DESC", pageNo, pageSize);
		for(Map<String, Object> map : list){
			if(map.get("RESULT") != null){
				if(map.get("NAME") != null){
					if(map.get("NAME").toString().equals("test0")){
						if(map.get("RESULT").toString().equals("0") || map.get("RESULT").toString().equals("-3")){
							map.put("ISSUCC", "错误");
						}else if(map.get("RESULT").toString().equals("1") || map.get("RESULT").toString().equals("-4")){
							map.put("ISSUCC", "正确");
						}
					}else if(map.get("NAME").toString().equals("test1")){
						if(map.get("RESULT").toString().equals("0") || map.get("RESULT").toString().equals("-3")){
							map.put("ISSUCC", "正确");
						}else if(map.get("RESULT").toString().equals("1") || map.get("RESULT").toString().equals("-4")){
							map.put("ISSUCC", "错误");
						}
					}else{
						map.put("ISSUCC", "");
					}
				}
				
				if(map.get("RESULT").toString().equals("0") || map.get("RESULT").toString().equals("-3")){
					map.put("RESULT", "同一人");
				}else if(map.get("RESULT").toString().equals("1") || map.get("RESULT").toString().equals("-4")){
					map.put("RESULT", "不同人");
				}
				
			} 
		}
		return list;
	}
	
	public int queryRecordTotal(String name,String beginTime, String endTime, String result, String issucc){
		List<Object> params = new ArrayList<Object>();
		params.add(name);
//		params.add(beginTime);
//		params.add(endTime);
		
		String preparedSql = "select count(1)"
			+ " from watermarkfaceverifyrecord t"
			+ " where t.sendtime >= to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') "
			+ " and t.sendtime <= to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') "
			+ " and t.processtime is not null "
			+ " and t.recvtime is not null "
			+ " and t.username = ? ";
		
		if(!Check.IsStringNULL(result)){
			preparedSql += " and t.result in(?,?)";
			if(result.equals("0"))
			{
				params.add(result);
				params.add("-3");
			}else{
				params.add("1");
				params.add("-4");
			}
		}
		
		if(!Check.IsStringNULL(issucc)){
			if(issucc.equals("1")){
				preparedSql += " and ((t.name='test0' and t.result in (1,-4)) or (t.name='test1' and t.result in (0,-3)))";
			}else{
				preparedSql += " and ((t.name='test0' and t.result in (0,-3)) or (t.name='test1' and t.result in (1,-4)))";
			}
		}
		
		Object obj = querySingleValue(preparedSql,params);
		return StringUtil.parseInt(obj,0);
	}
	
	
	public Map<String, Object> getSimpleRecord(String guid){
		List<Object> params = new ArrayList<Object>();
		params.add(guid);
		
		String sql = "select * from WATERMARKFACEVERIFYRECORD t where guid = ?";
		List<Map<String, Object>> list = queryReturnListMap(sql,params);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
}
