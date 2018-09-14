package iql.web.web;

import iql.web.system.domain.User;
import iql.web.system.service.UserService;
import iql.web.util.MD5Util;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by admin on 2017/7/18.
 */
public class MCInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    public static Logger logger = Logger.getLogger("IQL");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return true;
        }
        if(!"".equals(request.getParameter("token")) && null != userService.findUserByToken(request.getParameter("token").trim())){
            return true;
        }
        response.sendRedirect("/page/login");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) {

        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        //modelAndView.addObject("executeTime", executeTime);
        String s = "";
        if (handler != null && handler.toString().split(" ").length > 2) {
            s = handler.toString().split(" ")[2];
        } else {
            s = handler.toString();
        }
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String menu = (String) session.getAttribute("menu");

        if (menu == null) {
            menu = "IQL";
        }
        if (handler.toString().indexOf("login") > 0) {
            menu = "登录";
        }

        if (user != null) {
            logger.info("《系统》-用户：" + user.getReal_name() + "::::::::::::" + menu + "<<<<<<<<[##--" + s + "--##]>>>>>>>>  《执行时间 》: " + executeTime + "ms");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
