package iql.web.system.dao;

import iql.web.system.domain.Role;

import java.util.List;
import java.util.Map;

public interface RoleDao {

	public List<Map> findRoleMenuByIds(List<Integer> ids);

	public List<Map> findAllRole();

	public Integer addRole(Role role);

	public Integer updateRole(Role role);

	public Integer delRole(Role role);

	public Integer delRoleMenu(Role role);

	public Integer addRoleMenuAndDataSource(Role role, String menus, String dataSources);

	public List<Map> findRoleMenuByRoleId(Role role);
	
}
