package com.my.dao;

import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.my.common.OcrReturn;
import com.my.dao.base.CommonDao;
import com.my.util.GUID;
import com.my.util.Log4jUtil;

@Component
public class OCRDao extends CommonDao {
	
	public boolean saveOCRLog(Map<String,String> ocrMap, OcrReturn or){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int ret = 0;
		boolean defaultCommit = true;
		try{
			String mobile = ocrMap.get("mobile");
			if(mobile != null && mobile.length() > 11){
				mobile = mobile.substring(0, 11);
			}
			String preparedSql = "INSERT INTO T_OCR_HISTORY (GUID,MOBILE,BAR_CODE,ID_PHOTO,NAME,ID_NUMBER,SUCCESS,HOLD_TIME) VALUES (?,?,?,EMPTY_CLOB(),?,?,?,?)";
			String guid = new GUID().toString();
			
			conn = getConnection();
			defaultCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			
			pstmt = conn.prepareStatement(preparedSql);
			pstmt.setObject(1, guid);
			pstmt.setObject(2, mobile);
			pstmt.setObject(3, ocrMap.get("bar_code"));
			pstmt.setObject(4, or.getName());
			pstmt.setObject(5, or.getId_number());
			pstmt.setObject(6, or.isSuccess() ? "1" : "0");
			pstmt.setObject(7, new Date().getTime() - or.getBeginDate().getTime());
			ret = pstmt.executeUpdate();
			
			if( ret > 0 ){
				pstmt = conn.prepareStatement("SELECT ID_PHOTO FROM T_OCR_HISTORY WHERE GUID = ? FOR UPDATE");
				pstmt.setObject(1, guid);
				rs = pstmt.executeQuery();
				if (rs.next()){
					//�õ�java.sql.Clob�����ǿ��ת��Ϊoracle.sql.CLOB
					oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob("ID_PHOTO");
					Writer outStream = clob.getCharacterOutputStream();
					//data�Ǵ�����ַ��������壺String data
					char[] c = ocrMap.get("id_photo").toCharArray();
					outStream.write(c, 0, c.length);
					
					outStream.flush(); 
					outStream.close(); 
				}
			}
			conn.commit();
		}catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				Log4jUtil.log.error("���ݿⱣ�����֤OCRʶ���¼ʱ�쳣���ع�ʧ��", e1);
			}
			e.printStackTrace();
			Log4jUtil.log.error("���ݿⱣ�����֤OCRʶ���¼ʱ�쳣", e);
			
			return false;
		}finally{
			try {
				conn.setAutoCommit(defaultCommit);
			} catch (SQLException e) {
				e.printStackTrace();
				Log4jUtil.log.error("���ݿⱣ�����֤OCRʶ���¼ʱ�쳣,�ָ������ύ�쳣", e);
			}
			closeRes(rs,pstmt,conn);
		}
		return true;
	}
}
