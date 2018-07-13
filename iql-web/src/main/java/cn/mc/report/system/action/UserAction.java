package cn.mc.report.system.action;


import cn.mc.report.bean.BaseBean;
import cn.mc.report.system.domain.EntityUser;
import cn.mc.report.system.domain.User;
import cn.mc.report.system.service.UserService;
import cn.mc.report.util.MD5Util;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserAction {

	@Autowired
	private UserService userService;

	@RequestMapping(value="/system/list", method=RequestMethod.GET)
	public JSONObject findUserList(BaseBean base) {
		return userService.findUserList(base);
	}

	@RequestMapping(value="/system/findUserById", method=RequestMethod.GET)
	public JSONObject findUserById(EntityUser user){
		return userService.findUserById(user);
	}

	@RequestMapping(value="/system/checkusername", method=RequestMethod.POST)
	public Boolean checkUserName(EntityUser user){
		return userService.findByUsername(user)==null ? false : true;
	}

	@RequestMapping(value="/system/checkusernameandpw", method=RequestMethod.POST)
	public Boolean checkUserNameAndPassWord(HttpServletRequest request,EntityUser user){
		HttpSession session = request.getSession();
		User u  = (User)session.getAttribute("user");
		if(u==null){
			u = userService.findByUsername(user);
		}
		return u==null||!u.getPassword().equals(MD5Util.getMD5String(user.getPassword())) ? false : true;
	}

	@RequestMapping(value="/system/add", method=RequestMethod.POST)
	public Integer addUser(EntityUser user,String roles){
		return userService.addUser(user,roles);
	}

	@RequestMapping(value="/system/update", method=RequestMethod.POST)
	public Integer updateUser(EntityUser user,String roles){
		return userService.updateUser(user,roles);
	}

	@RequestMapping(value="/system/changepw", method=RequestMethod.POST)
	@Transactional
	public Integer changepw(HttpServletRequest request,EntityUser user){
		HttpSession session = request.getSession();
		User u  = (User)session.getAttribute("user");
		user.setId(u.getId());
		return userService.changePassWord(user);
	}

	@RequestMapping(value="/system/delete", method=RequestMethod.GET)
	public Integer delUser(EntityUser user){
		return userService.delUser(user);
	}

	@RequestMapping(value="/system/select", method= RequestMethod.GET)
	public JSONObject findUserSelect(EntityUser user){
		return userService.findUserSelect(user);
	}


	
}
