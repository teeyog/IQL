package cn.i4.report.system.dao;

import java.util.List;
import java.util.Map;

public interface JsonDataDao {

	public List<Map> findJsonDataByType(Integer type);
	
	public List<Map> findHomeMenuList();
	
	public List<Map> findSubmenuByIds(List<String> menuids);
	
	public List<Map> findRootmenuByIds(List<String> menuids);
	
}
