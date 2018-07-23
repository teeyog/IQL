package cn.i4.report.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.i4.report.util.TreeBuilder;
import cn.i4.report.system.domain.EntityUser;
import cn.i4.report.system.domain.Menu;
import cn.i4.report.system.domain.User;
import cn.i4.report.system.service.DictionaryService;
import cn.i4.report.system.service.UserService;
import cn.i4.report.system.vo.DictionaryVo;
import cn.i4.report.util.MD5Util;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;

import cn.i4.report.system.service.MenuService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WebController {

	@Autowired
	private MenuService menuService;

	@Autowired
	private UserService userService;

	@Autowired
	private DictionaryService dictionaryService;
	
	@RequestMapping("/")
    public ModelAndView index(HttpServletRequest request) {
		HttpSession session = request.getSession();
		ModelAndView mv = new ModelAndView();
		User u = (User)session.getAttribute("user");
		if(u==null){
			mv.setViewName("redirect:/page/login");
		}else{
			List<Menu> list = menuService.findMenuListByUid(u);//menuService.findAllMenuList();
			JSONArray rows = JSON.parseArray(JSON.toJSONString(list));
			JSONArray menuJSON = new TreeBuilder().buildTree(rows);
			mv.setViewName("index");
			mv.addObject("menu", menuJSON);
			session.setAttribute("usermenus",list);
		}
        return mv;
    }
	
	@RequestMapping("/login")
    public ModelAndView login(HttpServletRequest request,String username,String password) {
		HttpSession session = request.getSession();
		ModelAndView mv = new ModelAndView();
		EntityUser user = new EntityUser();
		user.setUsername(username);
		User u = userService.findByUsername(user);

		if(u!=null&&MD5Util.getMD5String(password).equals(u.getPassword())){
			if(u.getIsfirstlogin()==0){
				mv.setViewName("forward:/page/firstlogin");
				mv.addObject("username", username);
			}else{
				session.setAttribute("user",u);
				DictionaryVo vo = new DictionaryVo();
				vo.setName("idfa_delete_permission");//idfa删除权限标识符
				session.setAttribute("dict",dictionaryService.findfindDictionaryByName(vo));
				mv.setViewName("redirect:/");
			}
		}else{
			mv.setViewName("redirect:/page/login");
			mv.addObject("error", true);
		}
		return mv;
    }

	@RequestMapping("/logout")
    public ModelAndView loginOut(HttpServletRequest request){
		HttpSession session = request.getSession();
		ModelAndView mv = new ModelAndView();
		session.invalidate();
		mv.setViewName("redirect:/page/login");
		mv.addObject("logout", true);
		return mv;
	}

	@RequestMapping("/changepw")
	@Transactional
	public ModelAndView changePassWord(HttpServletRequest request,EntityUser user,String npassword){
		HttpSession session = request.getSession();
		ModelAndView mv = new ModelAndView();
		User u = userService.findByUsername(user);
		user.setId(u.getId());
		user.setIsfirstlogin(1);
		user.setPassword(npassword);
		userService.changeFirstLoginAndPassWord(user);
		u = userService.findByUsername(user);
		session.setAttribute("user",u);
		mv.setViewName("redirect:/");
		return mv;
	}
	
	@RequestMapping("/page/**")
	public String getPage(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String uri = request.getRequestURI();
		User user = (User) session.getAttribute("user");
		uri = uri.replaceAll("/page/", "");
		List<String> urlList = new ArrayList<String>();
		List<String> extUrl = new ArrayList<String>();
		extUrl.add("login");
		extUrl.add("404");
		extUrl.add("500");
		extUrl.add("home");
		extUrl.add("head");
		extUrl.add("index");
		extUrl.add("firstlogin");
		if(uri.indexOf("_v")>=0||extUrl.contains(uri)){
			return uri;
		}
		if(user!=null){
			List<Menu> list = (List<Menu>) session.getAttribute("usermenus");
			for(Menu m:list){
				if(m.getUrl()!=null){
					urlList.add(m.getUrl().trim());
				}
			}
			if(urlList.contains(uri)){
				return uri;
			}
		}
		return "403";
	}

	@RequestMapping("/menuclick")
	@ResponseBody
	public void menuClick(HttpServletRequest request,String menu) {
		request.getSession().setAttribute("menu",menu);
	}
}
