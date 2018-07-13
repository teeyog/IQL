package cn.mc.report.system.dao;

import cn.mc.report.bean.BaseBean;
import cn.mc.report.system.domain.EntityUser;
import cn.mc.report.system.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

	public User findByUsername(String username);

	public Integer findCount();

	public List<Map> findUserList(BaseBean base);

	public EntityUser findUserById(EntityUser user);

	public List<Map> findUserRole(EntityUser user);

	public Integer addUser(EntityUser user);

	public Integer updateUser(EntityUser user);

	public Integer delUser(EntityUser user);

	public Integer addUserRole(EntityUser user,String roles);

	public Integer delUserRole(EntityUser user);

	public List<Map> findAllUser(EntityUser user);
	
}
