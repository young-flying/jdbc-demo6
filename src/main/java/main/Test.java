package main;

import java.util.List;

import dao.IStuctDao;
import dao.StuctDao;

public class Test {
	public static void main(String[] args) {
		String sql = "select * from user ";
		IStuctDao stuctDao = new StuctDao();
		
		List<List<String>> totalList = stuctDao.queryTableSql(sql);
		
		for(List<String> itemsList: totalList) {
			if(null != itemsList && !itemsList.isEmpty()) {
				for(String item: itemsList) {
					System.out.print(item+"\t");
				}
				System.out.println();
			}
		}
	}
}
