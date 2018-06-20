package com.my.dao.base;

import java.sql.Connection;
import java.sql.SQLException;

import com.alibaba.druid.pool.DruidDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.my.common.ReadSetting;

public class JdbcUtils {
	private static final  ComboPooledDataSource dataSource = new ComboPooledDataSource();
	
	/*static {
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		String con_url = ReadSetting.getInstance().getCon_url();
		String name  = ReadSetting.getInstance().getConn_user(); 
		String password = ReadSetting.getInstance().getConn_pwd();
		dataSource.setUrl(ReadSetting.getInstance().getCon_url());
		dataSource.setName(ReadSetting.getInstance().getConn_user());
		dataSource.setPassword(ReadSetting.getInstance().getConn_pwd());
		dataSource.setInitialSize(10);
		dataSource.setMinIdle(10);
		dataSource.setMaxActive(20);
		dataSource.setMaxWait(60000l);
		dataSource.setTimeBetweenEvictionRunsMillis(300000l);
		dataSource.setTestOnReturn(false);
		dataSource.setTestOnBorrow(false);
		dataSource.setTestWhileIdle(true);
		try {
			dataSource.init();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	private static final ThreadLocal<Connection> local = new ThreadLocal<Connection>();
	
	public static Connection getConnection() throws SQLException{
		if(local.get()==null){
			Connection conn = dataSource.getConnection();
			local.set(conn);
		}
		return local.get();
	}
	
	public static void closeConn(){
		local.set(null);
	}

}
