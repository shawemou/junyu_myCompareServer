package com.my.dao;

import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.my.common.CompareReturn;
import com.my.dao.base.CommonDao;
import com.my.util.Check;
import com.my.util.GUID;
import com.my.util.Log4jUtil;

/**
 * @ClassName: CompareDao 
 * @Description: 暂存比对服务dao 
 */
@Component
public class CacheDao extends CommonDao {
	
	public List<Map<String, Object>> getList(Map<String,String> completeMap){
		String sql = "SELECT T.GUID,T.NAME,T.ID_NUMBER,T.SUCCESS CODE,T.SOURCE_INFO FROM T_CACHE_HISTORY T WHERE T.BAR_CODE = ? AND T.ETYPE = '1' ORDER BY T.CREATE_TIME ASC";
		List<Object> params = new ArrayList<Object>();
		params.add(completeMap.get("bar_code"));
		return queryReturnListMap(sql,params);
	}
	
	
	public boolean saveCacheLog(Map<String,String> compareMap, CompareReturn cr){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int ret = 0;
		boolean defaultCommit = true;
		try{
			String mobile = compareMap.get("mobile");
			if(mobile != null && mobile.length() > 11){
				mobile = mobile.substring(0, 11);
			}
			String preparedSql = "INSERT INTO T_CACHE_HISTORY (GUID,MOBILE,BAR_CODE,ID_TYPE,NAME,ID_NUMBER,SIMILARITY,SUCCESS,RETURN_CODE,HOLD_TIME,SOURCE_CODE,SOURCE_INFO,SOURCE_HOLD_TIME)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			String guid = new GUID().toString();
			
			conn = getConnection();
			defaultCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(preparedSql);
			pstmt.setObject(1, guid);
			pstmt.setObject(2, mobile);
			pstmt.setObject(3, compareMap.get("bar_code"));
			pstmt.setObject(4, compareMap.get("id_type"));
			pstmt.setObject(5, compareMap.get("name"));
			pstmt.setObject(6, compareMap.get("id_number"));
			
			pstmt.setObject(7, cr.getSimilarity());
			pstmt.setObject(8, cr.getCode());
			pstmt.setObject(9, cr.getReturn_code());
			pstmt.setObject(10, new Date().getTime() - cr.getBeginDate().getTime());
			
			pstmt.setObject(11, cr.getsCode());
			pstmt.setObject(12, cr.getsInfo());
			pstmt.setObject(13, cr.getSholdTime());
			ret = pstmt.executeUpdate();
			
//			if(!Check.IsStringNULL(cr.getCode()) && cr.getCode().equals("1")){

				String guid_history = new GUID().toString();
				String sql_photo = "INSERT INTO T_CACHE_HISTORY_PHOTO (GUID,HISTORY_GUID,PHOTO_ID,PHOTO_USER)"
					+ " VALUES (?,?,EMPTY_CLOB(),EMPTY_CLOB())";
				pstmt = conn.prepareStatement(sql_photo);
				pstmt.setObject(1, guid_history);
				pstmt.setObject(2, guid);
				ret = pstmt.executeUpdate();
				
				if( ret > 0 ){
					pstmt = conn.prepareStatement("SELECT PHOTO_ID,PHOTO_USER FROM T_CACHE_HISTORY_PHOTO WHERE GUID = ? FOR UPDATE");
					pstmt.setObject(1, guid_history);
					rs = pstmt.executeQuery();
					if (rs.next()){
						//得到java.sql.Clob对象后强制转换为oracle.sql.CLOB
						oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob("PHOTO_ID");
						Writer outStream = clob.getCharacterOutputStream();
						//data是传入的字符串，定义：String data
						char[] c = compareMap.get("photo_id").toCharArray();
						outStream.write(c, 0, c.length);
						
						outStream.flush(); 
						outStream.close(); 
						
						//得到java.sql.Clob对象后强制转换为oracle.sql.CLOB
						oracle.sql.CLOB clob_user = (oracle.sql.CLOB) rs.getClob("PHOTO_USER");
						Writer outStream_user = clob_user.getCharacterOutputStream();
						//data是传入的字符串，定义：String data
						char[] c_user = compareMap.get("photo_user").toCharArray();
						outStream_user.write(c_user, 0, c_user.length);
						
						outStream_user.flush(); 
						outStream_user.close(); 
					}
				}
//			}			
			
			conn.commit();
		}catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				Log4jUtil.log.error("数据库保存暂存比对记录时异常，回滚失败", e1);
			}
			e.printStackTrace();
			Log4jUtil.log.error("数据库保存暂存比对记录时异常", e);
			
			return false;
		}finally{
			try {
				conn.setAutoCommit(defaultCommit);
			} catch (SQLException e) {
				e.printStackTrace();
				Log4jUtil.log.error("数据库保存暂存比对记录时异常,恢复数据提交异常", e);
			}
			closeRes(rs,pstmt,conn);
		}
		return true;
	}
	
	/**
	 * 删除缓存数据
	 * @param list
	 * @return
	 */
	public int delete(List<String> list){
		List<Object> params = new ArrayList<Object>();
		String in = "";
		for(String bar_code : list){
			params.add(bar_code);
			in += (in.length() > 0 ? "," : "") + "?";
		}
		String sql = "UPDATE T_CACHE_HISTORY T SET T.ETYPE='0' WHERE T.GUID IN ( " + in + ")";
		return modify(sql,params);
	}
	
	public boolean done(String bar_code){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean defaultCommit = true;
		try{
			conn = getConnection();
			defaultCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			
			String sql = "INSERT INTO T_COMPARE_HISTORY (GUID,MOBILE,BAR_CODE,NAME,ID_NUMBER,SIMILARITY,SUCCESS,RETURN_CODE,HOLD_TIME,ID_TYPE,SOURCE_CODE,SOURCE_INFO,SOURCE_HOLD_TIME) " 
				+ " SELECT GUID,MOBILE,BAR_CODE,NAME,ID_NUMBER,SIMILARITY,SUCCESS,RETURN_CODE,HOLD_TIME,ID_TYPE,SOURCE_CODE,SOURCE_INFO,SOURCE_HOLD_TIME "
				+ " FROM T_CACHE_HISTORY T WHERE T.ETYPE='1' AND T.BAR_CODE = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setObject(1, bar_code);
			
			if(pstmt.executeUpdate() > 0){
				String sql2 = "INSERT INTO T_COMPARE_HISTORY_PHOTO (GUID,HISTORY_GUID,PHOTO_ID,PHOTO_USER)"
					+ " SELECT GUID,HISTORY_GUID,PHOTO_ID,PHOTO_USER FROM T_CACHE_HISTORY_PHOTO "
					+ " WHERE HISTORY_GUID IN (SELECT GUID FROM T_CACHE_HISTORY WHERE ETYPE='1' AND BAR_CODE = ?)";
				pstmt = conn.prepareStatement(sql2);
				pstmt.setObject(1, bar_code);
				pstmt.executeUpdate();
			}
			String sql3 = "UPDATE T_CACHE_HISTORY T SET T.ETYPE='2' WHERE T.BAR_CODE =?";
			pstmt = conn.prepareStatement(sql3);
			pstmt.setObject(1, bar_code);
			pstmt.executeUpdate();
			
			conn.commit();
		}catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				Log4jUtil.log.error("暂存数据转正式时异常，回滚失败", e1);
			}
			e.printStackTrace();
			Log4jUtil.log.error("暂存数据转正式时异常", e);
			
			return false;
		}finally{
			try {
				conn.setAutoCommit(defaultCommit);
			} catch (SQLException e) {
				e.printStackTrace();
				Log4jUtil.log.error("暂存数据转正式时异常,恢复数据提交异常", e);
			}
			closeRes(rs,pstmt,conn);
		}
		return true;
	} 
}
