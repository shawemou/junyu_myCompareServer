package com.my.dao.base;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListMapResultSetExtractor {

	public List<Map<String,Object>> extractData(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		List<Map<String,Object>> dt = new ArrayList<Map<String,Object>>();

		//for(int i=1;i<=meta.getColumnCount();i++){
		//	dt.addColumn(meta.getColumnName(i).toUpperCase());
		//}
		Map<String,Object> dr=null;
		while (rs.next()) {
			dr=new HashMap<String,Object>();
			dt.add(dr);
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				//System.out.println(meta.getColumnType(i));
				//System.out.println(meta.getColumnName(i).toUpperCase());
				switch (meta.getColumnType(i)) {
				case java.sql.Types.BIT:
					boolean b = rs.getBoolean(i);
					if (rs.wasNull()) {
						b = false;
					}
					dr.put(meta.getColumnName(i).toUpperCase(), new Boolean(b));
					break;
				case java.sql.Types.INTEGER:
				case java.sql.Types.TINYINT:
				case java.sql.Types.SMALLINT:
					int x = rs.getInt(i);
					if (rs.wasNull()) {
						dr.put(meta.getColumnName(i).toUpperCase(), new Integer(0));
					} else {
						dr.put(meta.getColumnName(i).toUpperCase(), new Integer(x));
					}
					break;
				case java.sql.Types.DECIMAL:
				case java.sql.Types.DOUBLE:
				case java.sql.Types.FLOAT:
					double t = rs.getDouble(i);
					if (rs.wasNull()) {
						dr.put(meta.getColumnName(i).toUpperCase(), new Double(0));
					} else {
						dr.put(meta.getColumnName(i).toUpperCase(), new Double(t));
					}
					break;
				case java.sql.Types.CHAR:
				case java.sql.Types.VARCHAR:
				case java.sql.Types.NVARCHAR:
					String k = rs.getString(i);
					if (rs.wasNull()) {
						dr.put(meta.getColumnName(i).toUpperCase(), "");
					} else {
						dr.put(meta.getColumnName(i).toUpperCase(), k);
					}
					break;
				case java.sql.Types.DATE:
				case java.sql.Types.TIMESTAMP:
					if (rs.getDate(i) != null) {
						// dr.put(meta.getColumnName(i).toUpperCase(),rs.getDate(i));
						dr.put(meta.getColumnName(i).toUpperCase(), rs.getTimestamp(i));
					}
					break;
				case java.sql.Types.BINARY:
				case java.sql.Types.VARBINARY://
					byte[] bv = rs.getBytes(i);
					if (rs.wasNull()) {
						dr.put(meta.getColumnName(i).toUpperCase(), new byte[] {});
					} else {
						dr.put(meta.getColumnName(i).toUpperCase(),
								DBUtil.bytesToString(bv, "UTF-8"));
					}
					break;
				case java.sql.Types.BLOB:
				case java.sql.Types.LONGVARBINARY:
					byte[] bs = rs.getBytes(meta.getColumnName(i).toUpperCase());
					if (bs == null) {
						dr.put(meta.getColumnName(i).toUpperCase(), "".getBytes());
					} else {
						dr.put(meta.getColumnName(i).toUpperCase(), bs);
					}
					break;
				case java.sql.Types.CLOB:
				case java.sql.Types.NCLOB:
					Object obj1 = rs.getObject(meta.getColumnName(i).toUpperCase());
					if (obj1 instanceof Clob) {
						obj1 = DBUtil.clobToString((Clob) obj1);
					}
					if (rs.wasNull()) {
						dr.put(meta.getColumnName(i).toUpperCase(), "");
					} else {
						dr.put(meta.getColumnName(i).toUpperCase(), obj1);
					}
					break;
				default:
					Object deObj = rs.getObject(meta.getColumnName(i).toUpperCase());
					if (rs.wasNull()) {
						dr.put(meta.getColumnName(i).toUpperCase(), "");
					} else {
						dr.put(meta.getColumnName(i).toUpperCase(), deObj);
					}
					break;
				}
			}
		}
		return dt;
	}

}
