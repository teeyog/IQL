package iql.web.system.action;


import iql.web.bean.BaseBean;
import iql.web.system.domain.User;
import iql.web.system.service.UserService;
import iql.web.util.MD5Util;
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
	public JSONObject findUserById(User user){
		return userService.findUserById(user);
	}

	@RequestMapping(value="/system/checkusername", method=RequestMethod.POST)
	public Boolean checkUserName(User user){
		return userService.findByUsername(user)==null ? false : true;
	}

	@RequestMapping(value="/system/checkusernameandpw", method=RequestMethod.POST)
	public Boolean checkUserNameAndPassWord(HttpServletRequest request,User user){
		HttpSession session = request.getSession();
		User u  = (User)session.getAttribute("user");
		if(u==null){
			u = userService.findByUsername(user);
		}
		return u==null||!u.getPassword().equals(MD5Util.getMD5String(user.getPassword())) ? false : true;
	}

	@RequestMapping(value="/system/add", method=RequestMethod.POST)
	public Integer addUser(User u,String roles){
		String token = MD5Util.getMD5String(u.getUsername() + u.getPassword() + System.currentTimeMillis());
		u.setToken(token);
		return userService.addUser(u,roles);
	}

	@RequestMapping(value="/system/update", method=RequestMethod.POST)
	public Integer updateUser(User user,String roles){
		return userService.updateUser(user,roles);
	}

	@RequestMapping(value="/system/changepw", method=RequestMethod.POST)
	@Transactional
	public Integer changepw(HttpServletRequest request,User user){
		HttpSession session = request.getSession();
		User u  = (User)session.getAttribute("user");
		user.setId(u.getId());
		return userService.changePassWord(user);
	}

	@RequestMapping(value="/system/delete", method=RequestMethod.GET)
	public Integer delUser(User user){
		return userService.delUser(user);
	}

	@RequestMapping(value="/system/select", method= RequestMethod.GET)
	public JSONObject findUserSelect(User user){
		return userService.findUserSelect(user);
	}

	@RequestMapping(value="/system/token", method= RequestMethod.GET)
	public JSONObject getToken(HttpServletRequest request){
		HttpSession session = request.getSession();
		User u  = (User)session.getAttribute("user");
		User newUser = userService.findByUsername(u);
		JSONObject obj = new JSONObject();
		obj.put("token",newUser.getToken());
		obj.put("uid",u.getId());
		return obj;
	}

	@RequestMapping(value="/system/deleteToken", method= RequestMethod.POST)
	public Integer deleteToken(HttpServletRequest request){
		HttpSession session = request.getSession();
		User u  = (User)session.getAttribute("user");
		return userService.updateToken(u,"");
	}

	@RequestMapping(value="/system/generateToken", method= RequestMethod.GET)
	public Integer generateToken(HttpServletRequest request){
		HttpSession session = request.getSession();
		User u  = (User)session.getAttribute("user");
		return userService.updateToken(u,MD5Util.getMD5String(u.getUsername() + u.getPassword() + System.currentTimeMillis()));
	}
}
