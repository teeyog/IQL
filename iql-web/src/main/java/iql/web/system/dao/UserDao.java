package iql.web.system.dao;

import iql.web.bean.BaseBean;
import iql.web.system.domain.User;
import iql.web.system.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

	public User findByUsername(String username);

	public Integer findCount();

	public List<Map> findUserList(BaseBean base);

	public User findUserById(User user);

	public List<Map> findUserRole(User user);

	public Integer addUser(User user);

	public Integer updateUser(User user);

	public Integer delUser(User user);

	public Integer addUserRole(User user,String roles);

	public Integer delUserRole(User user);

	public List<Map> findAllUser(User user);

	public Integer updateToken(User user,String token);

	public User findUserByToken(String token);
	
}
