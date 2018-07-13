package cn.mc.report.system.dao;

import cn.mc.report.system.domain.Role;

import java.util.List;
import java.util.Map;

public interface RoleDao {

	public List<Map> findRoleMenuByIds(List<Integer> ids);

	public List<Map> findAllRole();

	public Integer addRole(Role role);

	public Integer updateRole(Role role);

	public Integer delRole(Role role);

	public Integer delRoleMenu(Role role);

	public Integer addRoleMenu(Role role, String menus);

	public List<Map> findRoleMenuByRoleId(Role role);
	
}
