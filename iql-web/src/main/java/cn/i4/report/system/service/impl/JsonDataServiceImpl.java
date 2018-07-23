package cn.i4.report.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.i4.report.system.service.JsonDataService;
import cn.i4.report.util.TreeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.i4.report.system.dao.JsonDataDao;

@Service
@Transactional
public class JsonDataServiceImpl implements JsonDataService {

	@Autowired
	private JsonDataDao jsonDataDao;
	
	@Override
	public JSONObject findJsonDataByType(Integer type) {
		JSONObject res = new JSONObject();
		List<Map> list = jsonDataDao.findJsonDataByType(type);
		JSONArray rows = JSON.parseArray(JSON.toJSONString(list));
		JSONArray options = new TreeBuilder(rows).buildTree();
		res.put("options", options);
		return res;
	}

	@Override
	public JSONArray findHomeMenuList() {
		List<Map> list = jsonDataDao.findHomeMenuList();
		return JSON.parseArray(JSON.toJSONString(list));
	}

	@Override
	public JSONObject findAllSubMenuByIds(List<String> menuids) {
		JSONObject res = new JSONObject();
		boolean b = true;
		List<String> menuidList = new ArrayList<>(menuids);
		while(b) {
			List<Map> subMenu = jsonDataDao.findSubmenuByIds(menuidList);
			if(subMenu!=null && subMenu.size()>0) {
				menuidList.clear();
				JSONArray rows = JSON.parseArray(JSON.toJSONString(subMenu));
				for(int i=0; i<rows.size(); i++) {
					JSONObject row = rows.getJSONObject(i);
					menuidList.add(row.getString("id"));
					if(res.containsKey(row.getString("pid"))) {
						res.put(row.getString("id"), res.getString(row.getString("pid"))+"-"+row.getString("name"));
					} else {
						res.put(row.getString("id"), row.getString("name"));
					}
				}
			} else {
				b = false;
			}
		}
		return res;
	}

	@Override
	public JSONObject findAllRootMenuByIds(List<String> menuids) {
		JSONObject res = new JSONObject();
		JSONObject pido = new JSONObject();
		boolean b = true;
		List<String> menuidList = new ArrayList<>(menuids);
		while(b) {
			if(menuidList.isEmpty()) 
				break;
			List<Map> rootMenu = jsonDataDao.findRootmenuByIds(menuidList);
			if(rootMenu!=null && rootMenu.size()>0) {
				menuidList.clear();
				JSONArray rows = JSON.parseArray(JSON.toJSONString(rootMenu));
				for(int i=0; i<rows.size(); i++) {
					JSONObject row = rows.getJSONObject(i);
					String pid = row.getString("pid");
					String id = row.getString("id");
					String name = row.getString("name");
					
					if(res.containsKey(id)) {
						continue;
					}
					
					if(!pid.equals("-1")) {
						menuidList.add(pid);
					}
					
					res.put(id, name);
					if(pido.containsKey(id)) {
						JSONArray subMenuids = pido.getJSONArray(id);
						for(int j=0; j<subMenuids.size(); j++) {
							res.put(subMenuids.getString(j), name+"-"+res.getString(subMenuids.getString(j)));
						}
						if(!pid.equals("-1")) {
							pido.getJSONArray(id).add(id);
							if(pido.containsKey(pid))
								pido.getJSONArray(pid).addAll(pido.getJSONArray(id));
							else
								pido.put(pid, pido.getJSONArray(id));
						}
						pido.remove(id);
					} else {
						if(!pid.equals("-1")) {
							if(!pido.containsKey(pid))
								pido.put(pid, new JSONArray());
							pido.getJSONArray(pid).add(id);
						}
					}
				}
			} else {
				b = false;
			}
		}
		return res;
	}

}
