package cn.i4.report.config;

import cn.i4.report.web.UserAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class MCWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationProvider userAuthenticationProvider; //自定义验证
	
	@Autowired
	private UserAuthenticationSuccessHandler successHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
        .authorizeRequests()
        	.antMatchers("/**").permitAll() //引入hplus资源不用登录
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .loginPage("/login")
            .successHandler(successHandler)
            .permitAll()
            .and()
        .logout()
        	.logoutUrl("/logout")
            .permitAll();
     
		http.csrf().disable().headers().frameOptions().sameOrigin(); //嵌入iframe
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//将验证过程交给自定义验证工具
		auth.authenticationProvider(userAuthenticationProvider);
	}
	
}
