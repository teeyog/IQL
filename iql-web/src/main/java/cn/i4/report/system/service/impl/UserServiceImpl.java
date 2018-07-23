package cn.i4.report.system.service.impl;

import cn.i4.report.util.DataUtil;
import cn.i4.report.bean.BaseBean;
import cn.i4.report.system.domain.EntityUser;

import cn.i4.report.system.domain.User;
import cn.i4.report.util.MD5Util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.i4.report.system.dao.UserDao;
import cn.i4.report.system.service.UserService;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public JSONObject findUserList(BaseBean base){
		JSONObject res = new JSONObject();
		List<Map> list = userDao.findUserList(base);
		int total = list.size();
		list = DataUtil.pageFormate(list,base.getOffset(),base.getLimit());
		JSONArray rows = JSON.parseArray(JSON.toJSONString(list));
		res.put("total", total);
		res.put("rows", rows);
		return res;
	}

	@Override
	public JSONObject findUserById(EntityUser user) {
		return JSON.parseObject(JSON.toJSONString(userDao.findUserById(user)));
	}

	@Override
	public User findByUsername(EntityUser user) {
		if(user==null||user.getUsername()==null){
			return null;
		}
		return userDao.findByUsername(user.getUsername().trim());
	}

	@Override
	public Integer addUser(EntityUser user,String roles) {
		user.setPassword(MD5Util.getMD5String(user.getPassword()));
		user.setIsfirstlogin(0);
		user.setCreate_time(new Date());
		userDao.addUser(user);
		userDao.addUserRole(user,roles);
		return 1;
	}

	@Override
	public Integer updateUser(EntityUser user,String roles) {
		if(!"@#$1-2-3-i4-5-6-7$#@".equals(user.getPassword().trim())){
			user.setPassword(MD5Util.getMD5String(user.getPassword()));
		}
		user.setCreate_time(new Date());
		userDao.delUserRole(user);
		userDao.addUserRole(user,roles);
		userDao.updateUser(user);
		return 1;
	}

	@Override
	public Integer delUser(EntityUser user) {
		userDao.delUser(user);
		userDao.delUserRole(user);
		return 1;
	}

	@Override
	public Integer changePassWord(EntityUser user) {
		EntityUser us = userDao.findUserById(user);
		us.setPassword(MD5Util.getMD5String(user.getPassword().trim()));
		return userDao.updateUser(us);
	}

	@Override
	public Integer changeFirstLoginFlag(EntityUser user) {
		EntityUser us = userDao.findUserById(user);
		us.setIsfirstlogin(user.getIsfirstlogin());
		return userDao.updateUser(us);
	}

	@Override
	public Integer changeFirstLoginAndPassWord(EntityUser user) {
		EntityUser us = userDao.findUserById(user);
		us.setIsfirstlogin(user.getIsfirstlogin());
		us.setPassword(MD5Util.getMD5String(user.getPassword().trim()));
		return userDao.updateUser(us);
	}

	@Override
	public JSONObject findUserSelect(EntityUser user) {
		JSONObject res = new JSONObject();
		JSONArray options = new JSONArray();
		List<Map> list = userDao.findAllUser(user);
		JSONArray rows = JSON.parseArray(JSON.toJSONString(list));
		for(int i=0; i<rows.size(); i++) {
			JSONObject row = rows.getJSONObject(i);
			JSONObject o = new JSONObject();
			o.put("value", row.getString("id"));
			o.put("label", row.getString("real_name"));
			options.add(o);
		}
		res.put("options", options);
		return res;
	}
}
