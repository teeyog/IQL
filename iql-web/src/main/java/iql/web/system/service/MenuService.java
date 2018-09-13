package iql.web.system.service;

import iql.web.bean.BaseBean;
import iql.web.system.domain.Menu;
import iql.web.system.domain.User;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface MenuService {

	public JSONArray findAllMenuList();

	public JSONObject findAllMenu(BaseBean base);

	public JSONObject findParentMenu();

	public JSONObject findChildMenu(Menu menu);

	public JSONObject findMenuSelect();

	public Integer addMenu(Menu menu);

	public Integer updateMenu(Menu menu);

	public Integer delMenu(Menu menu);

	public List<Menu> findMenuListByUid(User user);
	
}
