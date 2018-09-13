package iql.web.system.service;

import java.util.List;

import iql.web.bean.BaseBean;
import iql.web.system.domain.Role;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.transaction.annotation.Transactional;

public interface RoleService {

	public JSONObject findRoleMenuByIds(List<Integer> ids);

	public JSONObject findAllRole(BaseBean base);

    public JSONArray findRoleMenu(Role role);

	@Transactional
	public Integer addRole(Role role,String menus);

	@Transactional
	public Integer updateRole(Role role,String menus);

	@Transactional
	public Integer delRole(Role role);

	public JSONObject findRoleSelect();
	
}
