package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class StuctDao implements IStuctDao {
	private Connection conn;
	{
		try {
			String fileName="/jdbc.properties";//这里是指放在classes下，如果有包的话，前面加包名即可。例：/com/web/db.properties  
		    String driver = "";  
		    String url = "";  
		    String username ="";  
		    String password = "";  
		    InputStream in = StuctDao.class.getResourceAsStream(fileName);
		    Properties pts = new Properties();
		    pts.load(in);
		    in.close();  
            if(pts.containsKey("jdbc.driver")){  
                driver = pts.getProperty("jdbc.driver");  
            }  
            if(pts.containsKey("jdbc.url")){  
                url = pts.getProperty("jdbc.url");  
            }  
            if(pts.containsKey("jdbc.user")){  
                username = pts.getProperty("jdbc.user");  
            }  
            if(pts.containsKey("jdbc.password")){  
                password = pts.getProperty("jdbc.password");  
            }  
			Class.forName(driver);
			conn = DriverManager.getConnection(url,username,password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private List<String> queryColumnNames(String tableName) {
		List<String> columnNameList = new ArrayList<String>();
		ResultSet colRet = null;
		try {
			DatabaseMetaData m_DBMetaData = conn.getMetaData();
			colRet = m_DBMetaData.getColumns(null, "%", tableName,
					"%");
			while (colRet.next()) {
				columnNameList.add(colRet.getString("COLUMN_NAME"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(null != colRet) {
				try {
					colRet.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
		return columnNameList;
	}
	
	private String getTableName(String sql) {
		String name = null;
		if(sql.contains(" from ")) {
			String subSql = sql.substring(sql.indexOf(" from ")+6);
			if(subSql.contains(" ")) {
				name = subSql.substring(0,subSql.indexOf(" "));
			}else {
				name = subSql;
			}
		}
		return name;
	}
	@Override
	public List<List<String>> queryTableSql(String sql) {
		List<List<String>> topList = new ArrayList<List<String>>();
		if(null == sql || "".equals(sql)) {
			return topList;
		}
		String tableName = getTableName(sql);
		if(null == tableName || "".equals(tableName)) {
			return topList;
		}
		List<String> columnList = queryColumnNames(tableName);
		if(columnList == null || columnList.isEmpty()) {
			return topList;
		}
		topList.add(columnList);
		List<String> tempList = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				tempList = new ArrayList<String>();
				for(String column: columnList) {
					tempList.add(rs.getString(column));
				}
				
				topList.add(tempList);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return topList;
	}

	
}
