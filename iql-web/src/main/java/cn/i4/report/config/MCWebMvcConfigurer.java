package cn.i4.report.config;

import cn.i4.report.web.MCInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by admin on 2017/7/18.
 */
@Configuration
public class MCWebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new MCInterceptor())
                .addPathPatterns("/**")
                .addPathPatterns("/page/**")
                .excludePathPatterns("/mcdata/login")
                .excludePathPatterns("/mcdata/sync/run")
                .excludePathPatterns("/mcdata/tablesync/run")
                .excludePathPatterns("/mcdata/user/system/checkusernameandpw")
                .excludePathPatterns("/sync/run")
                .excludePathPatterns("/tablesync/run")
                .excludePathPatterns("/login")
                .excludePathPatterns("/changepw")
                .excludePathPatterns("/page/login")
                .excludePathPatterns("/page/404")
                .excludePathPatterns("/page/500")
                .excludePathPatterns("/page/firstlogin")
                .excludePathPatterns("/getActiveStreams")
                .excludePathPatterns("/query")
                .excludePathPatterns("/query2")
                .excludePathPatterns("/getresult")
                .excludePathPatterns("/jobScript/tree")
                .excludePathPatterns("/jobScript/update")
                .excludePathPatterns("/getStreamStatus")
                .excludePathPatterns("/stopStreamJob");

        super.addInterceptors(registry);
    }
}
