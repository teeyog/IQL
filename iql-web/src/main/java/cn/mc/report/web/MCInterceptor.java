package cn.mc.report.web;

import cn.mc.report.system.domain.User;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by admin on 2017/7/18.
 */
public class MCInterceptor implements HandlerInterceptor {

    public static Logger logger  =  Logger.getLogger("IQL");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        HttpSession session = request.getSession();
        User u = (User)session.getAttribute("user");
        if(u!=null){
            return true;
        }
        response.sendRedirect("/page/login");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) throws Exception {

        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        //modelAndView.addObject("executeTime", executeTime);
        String s = "";
        if(handler!=null&&handler.toString().split(" ").length>2){
            s=handler.toString().split(" ")[2];
        }else{
            s=handler.toString();
        }
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String menu = (String)session.getAttribute("menu");

        if(menu==null){
            menu="IQL";
        }
        if(handler.toString().indexOf("login")>0){
            menu="登录";
        }

        if(user!=null){
            logger.info("《系统》-用户："+user.getReal_name()+"::::::::::::"+menu+"<<<<<<<<[##--" + s + "--##]>>>>>>>>  《执行时间 》: " + executeTime + "ms");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
