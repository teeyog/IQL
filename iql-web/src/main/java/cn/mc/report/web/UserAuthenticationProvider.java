package cn.mc.report.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import cn.mc.report.system.domain.User;
import cn.mc.report.util.MD5Util;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserDetailsService userSecurityService;
	
	/**
     * 自定义验证方式
     */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        User user = (User) userSecurityService.loadUserByUsername(username);
        if(user == null){
            throw new BadCredentialsException("Username not found.");
        }
        
        //加密过程在这里体现
        if (!MD5Util.getMD5String(password).equals(user.getPassword())) {
            throw new BadCredentialsException("Wrong password.");
        }
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
