package cn.i4.report.system.service.impl;

import java.util.List;

import cn.i4.report.util.DataUtil;
import cn.i4.report.util.TreeBuilder;
import cn.i4.report.bean.BaseBean;
import cn.i4.report.system.domain.User;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.i4.report.system.dao.MenuDao;
import cn.i4.report.system.domain.Menu;
import cn.i4.report.system.service.MenuService;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuDao menuDao;
	
	@Override
	public JSONArray findAllMenuList() {
		List<Menu> list = menuDao.findAllMenuList();
		JSONArray rows = JSON.parseArray(JSON.toJSONString(list));
		JSONArray menuJSON = new TreeBuilder().buildTree(rows);
		return menuJSON;
	}

	@Override
	public JSONObject findAllMenu(BaseBean base) {
		List<Menu> list = menuDao.findAllMenuList();
		int total = list.size();
		list = DataUtil.pageFormate(list,base.getOffset(),base.getLimit());
		JSONArray rows = JSON.parseArray(JSON.toJSONString(list));
		JSONObject object = new JSONObject();
		object.put("total",total);
		object.put("rows",rows);
		return object;
	}

	@Override
	public JSONObject findParentMenu() {
		Menu menu = new Menu();
		menu.setPid(-1);
		List<Menu> list = menuDao.findMenuByPid(menu);
		int total = list.size();
		JSONArray rows = JSON.parseArray(JSON.toJSONString(list));
		JSONObject object = new JSONObject();
		object.put("total",total);
		object.put("rows",rows);
		return object;
	}

	@Override
	public JSONObject findChildMenu(Menu menu) {
		List<Menu> list = menuDao.findMenuByPid(menu);
		int total = list.size();
		JSONArray rows = JSON.parseArray(JSON.toJSONString(list));
		JSONObject object = new JSONObject();
		object.put("total",total);
		object.put("rows",rows);
		return object;
	}

	@Override
	public JSONObject findMenuSelect() {
		JSONObject res = new JSONObject();
		JSONArray options = new JSONArray();
		Menu menu = new Menu();
		menu.setPid(-1);
		List<Menu> list = menuDao.findMenuByPid(menu);
		JSONArray rows = JSON.parseArray(JSON.toJSONString(list));
		JSONObject first = new JSONObject();
		first.put("value", "-1");
		first.put("label", "一级菜单(无父级)");
		options.add(first);
		for(int i=0; i<rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			JSONObject o = new JSONObject();
			o.put("value", row.getString("id"));
			o.put("label", row.getString("name"));
			options.add(o);
		}
		res.put("options", options);
		return res;
	}

	@Override
	public Integer addMenu(Menu menu) {
		return menuDao.addMenu(menu);
	}

	@Override
	public Integer updateMenu(Menu menu) {
		return menuDao.updateMenu(menu);
	}

	@Override
	public Integer delMenu(Menu menu) {
		return menuDao.delMenu(menu);
	}

	@Override
	public List<Menu> findMenuListByUid(User user) {
		return menuDao.findMenuListByUid(user);
	}
}
