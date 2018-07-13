package cn.mc.report.system.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface JsonDataService {

	public JSONObject findJsonDataByType(Integer type);
	
	public JSONArray findHomeMenuList();
	
	public JSONObject findAllSubMenuByIds(List<String> menuids);
	
	public JSONObject findAllRootMenuByIds(List<String> menuids);
	
}
