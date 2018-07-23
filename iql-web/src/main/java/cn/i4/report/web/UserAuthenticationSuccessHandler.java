package cn.i4.report.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.i4.report.system.domain.Role;
import cn.i4.report.system.domain.User;
import cn.i4.report.system.service.RoleService;

@Component
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private RoleService roleService;
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();  
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		User user = (User) authentication.getPrincipal();
		
		// 获取菜单-多角色取并集
		List<Integer> ids = new ArrayList<>();
		List<String> roleNames = new ArrayList<>();
		for(Role role : user.getRoles()) {
			ids.add(role.getId());
			roleNames.add(role.getRolename());
		}
		JSONObject menu = roleService.findRoleMenuByIds(ids);
		
		HttpSession session = request.getSession();
		session.setAttribute("menu", menu);
		
		JSONObject u = new JSONObject();
		u.put("name", user.getReal_name());
		u.put("roles", roleNames);
		session.setAttribute("user", u);
		
		this.redirectStrategy.sendRedirect(request, response, "/");
	}
	
}
