package iql.web.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class MCWebServletConfigurer {
	
	@Bean
	public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
		ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
		registration.addUrlMappings("/");
		registration.addUrlMappings("/mcdata/*");
		return registration;
	}
	
	/*@Bean
	public ServletRegistrationBean apiV1ServletBean(WebApplicationContext webApplicationContext) {
		DispatcherServlet ds = new DispatcherServlet(webApplicationContext);
		ServletRegistrationBean bean = new ServletRegistrationBean(ds, "/mcdata/*");
		bean.setName("api-v1");
		return bean;
	}*/
	
}
