package iql.web.config;

import iql.web.system.service.UserService;
import iql.web.web.MCInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by admin on 2017/7/18.
 */
@Configuration
public class MCWebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Bean
    public MCInterceptor mcInterceptor(){
        return new MCInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(mcInterceptor())
                .addPathPatterns("/**")
                .addPathPatterns("/page/**")
                .excludePathPatterns("/mcdata/login")
                .excludePathPatterns("/mcdata/user/system/checkusernameandpw")
                .excludePathPatterns("/sync/run")
                .excludePathPatterns("/tablesync/run")
                .excludePathPatterns("/login")
                .excludePathPatterns("/changepw")
                .excludePathPatterns("/page/login")
                .excludePathPatterns("/page/404")
                .excludePathPatterns("/page/500")
                .excludePathPatterns("/page/firstlogin")
                ;

        super.addInterceptors(registry);
    }
}
