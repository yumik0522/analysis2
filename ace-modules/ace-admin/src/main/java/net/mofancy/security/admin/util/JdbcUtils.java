//package net.mofancy.security.admin.util;
//
//import net.mofancy.security.admin.config.DataSourceConfig;
//
//import javax.sql.DataSource;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class JdbcUtils {
//
//	public static List<Map<String,Object>> getDataPoolList(Integer id) {
//
//		DataSource dataSource = new DataSourceConfig().mainDataSource();
//
//		List<Map<String,Object>> list = new ArrayList<>();
//		try {
//			Connection conn = dataSource.getConnection();
//			String sql = "SELECT a.dataset_sys_key AS dataset_key,a.dataset_name,b.param_val AS dbname,c.param_val AS dburl,COALESCE(d.param_val,'public') AS dbschema,COALESCE(e.param_val,'postgres') AS dbpwd FROM dataset_list a\r\n" +
//					"INNER JOIN dataset_parameter b ON\r\n" +
//					"a.dataset_sys_key = b.dataset_sys_key AND b.param_name = 'DatabaseName'\r\n" +
//					"INNER JOIN dataset_parameter c ON\r\n" +
//					"a.dataset_sys_key = c.dataset_sys_key AND c.param_name = 'DatabaseServer'\r\n" +
//					" LEFT JOIN dataset_parameter d ON\r\n" +
//					" a.dataset_sys_key = d.dataset_sys_key AND d.param_name = 'DatabaseSchema'\r\n" +
//					"  LEFT JOIN dataset_parameter e ON\r\n" +
//					" a.dataset_sys_key = e.dataset_sys_key AND e.param_name = 'DatabasePwd' WHERE 1=1 ";
//			if(id!=null) {
//				sql += " AND a.dataset_sys_key = "+id+" ";
//			}
//			PreparedStatement pstmt;
//
//		 pstmt = conn.prepareStatement(sql);
//		 ResultSet rs = pstmt.executeQuery();
//		 ResultSetMetaData rsm =rs.getMetaData();
//		 int col = rs.getMetaData().getColumnCount();
//		 while (rs.next()) {
//			 Map<String,Object> map = new HashMap<String,Object>();
//			 for (int i = 1; i <= col; i++) {
//				 String value = rs.getString(i);
//				 map.put(rsm.getColumnName(i), value);
//			 }
//			 list.add(map);
//		 }
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//	    return list;
//	}
//
//	public static int getJobId() {
//		DataSource dataSource = new DataSourceConfig().mainDataSource();
//		int id = 0;
//		String sql = "SELECT nextval('jqueue_job_seq')";
//	    PreparedStatement pstmt;
//
//	    try {
//			Connection conn = dataSource.getConnection();
//			pstmt = conn.prepareStatement(sql);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				id = Integer.parseInt(rs.getString(1));
//			}
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//	    return id;
//	}
//
//}
