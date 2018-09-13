package iql.web.system.dao;

import java.util.List;

import iql.web.system.domain.Menu;
import iql.web.system.domain.User;

public interface MenuDao {

	public List<Menu> findAllMenuList();

	public Integer findCount();

	public Integer addMenu(Menu menu);

	public Integer updateMenu(Menu menu);

	public Integer delMenu(Menu menu);

	public List<Menu> findMenuListByUid(User user);

	public Menu findMenuById(Menu menu);

	public List<Menu> findMenuByPid(Menu menu);

}
