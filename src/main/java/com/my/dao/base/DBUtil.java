package com.my.dao.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Clob;

public class DBUtil {
	
	public static String bytesToString(byte[] bytes,String encoding){
		try {
			return new String(bytes,encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static String clobToString(Clob clob) {
		if (clob == null){
			return "";
		}
		String reString = "";
		Reader is;
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			is = clob.getCharacterStream();
			// 
			br = new BufferedReader(is);
			String s = br.readLine();
			while (s != null) {// 执行循环将字符串全部取出付给StringBuffer由StringBuffer转成STRING
				sb.append(s);
				sb.append("\r");
				s = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		reString = sb.toString();
		return reString;
	}
}
