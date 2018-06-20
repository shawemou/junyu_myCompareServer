package com.my.dao.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;
import com.my.common.ReadSetting;
import com.my.util.Log4jUtil;
import com.shawemou.controller.ReadingSetting;

@Component
public class CommonDao {

	@Autowired
	private ReadingSetting readingSetting;
	
	public Connection getConnection() throws SQLException{
		return JdbcUtils.getConnection();
	}
	
	public Object querySingleValue(String sql, List<Object> params){
		Connection conn=null;
		PreparedStatement stmt = null;
		ResultSet rs=null;
		Object ret = null;
		try{
			conn= getConnection();
			stmt = conn.prepareStatement(sql);
			for(int i=0;i<params.size();i++){
				stmt.setObject(i+1, params.get(i));
			}
			rs = stmt.executeQuery();
			if(rs.next()){
				ret=rs.getObject(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeRes(rs,stmt,conn);
		}
		return ret;
	}
	
	
	public List<Map<String, Object>> queryReturnListMap(String preparedSql,List<Object> params) {
		Connection conn = null;
		PreparedStatement stmt=null;
		ResultSet rs=null;
		System.out.println(preparedSql);
		try{
			conn= getConnection();
			stmt=conn.prepareStatement(preparedSql);
			for(int i=0;i<params.size();i++){
				stmt.setString(i+1, params.get(i).toString());
			}
			rs = stmt.executeQuery();
			return new ListMapResultSetExtractor().extractData(rs);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeRes(rs,stmt,conn);
		}
		return null;
	}
	
	public List<Map<String, Object>> queryReturnListMap(String preparedSql,List<Object> params,String orderBy, int pageNo, int pageSize) {
		String sql = wriePagingSql(preparedSql, pageNo, pageSize,orderBy);
		System.out.println(sql);
		Connection conn=null;
		PreparedStatement stmt=null;
		ResultSet rs=null;
		try{
			conn=getConnection();
			stmt=conn.prepareStatement(sql);
			for(int i=0;i<params.size();i++){
				stmt.setObject(i+1, params.get(i));
			}
			rs = stmt.executeQuery();
			return new ListMapResultSetExtractor().extractData(rs);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeRes(rs,stmt,conn);
		}
		return null;
	}
	

	public List<Map<String,Object>> queryReturnListMap(String sql){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			return new ListMapResultSetExtractor().extractData(rs);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeRes(rs,stmt,conn);
		}
		return null;
	}
	
	public String wriePagingSql(String sql, int pageNo, int pageSize, String orderBy) {
		orderBy=(null==orderBy)?"":orderBy;
		return new StringBuilder(" select * from (select t.*, rownum ro from (")
						 .append(sql)
						 .append(" ")
						 .append(orderBy.length() > 0 ? ("ORDER BY " + orderBy) : "")
						 .append(") t where rownum <=")
						 .append(((pageNo-1)*pageSize+pageSize))
						 .append(" ) where ro >= ")
						 .append(((pageNo-1)*pageSize+1))
						 .toString();
	}
	
	public int modify(String preparedSql, List<Object> params){
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int ret = 0;
		try{
			conn = getConnection();
			stmt= conn.prepareStatement(preparedSql);
			for(int i = 0; i < params.size(); i++){
				stmt.setObject(i + 1, params.get(i));
			}
			ret = stmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			closeRes(rs,stmt,conn);
		}
		
		return ret;
	}
	
	public void closeRes(ResultSet rs,Statement stmt,Connection conn){
		try {
			if(rs!=null){
				rs.close();
			}
			if(stmt!=null){
				stmt.close();
			}
			if(conn!=null){
				conn.close();
				JdbcUtils.closeConn();
			}			
		} catch (SQLException e) {
			e.printStackTrace();
			Log4jUtil.log.error("关闭数据连接异常", e);
		}
	}
}
