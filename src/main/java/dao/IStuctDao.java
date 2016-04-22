package dao;

import java.util.List;


public interface IStuctDao {
	/**
	 * 获取某个sql的查询信息
	 * @param sql
	 * @return
	 */
	List<List<String>> queryTableSql(String sql);
}
