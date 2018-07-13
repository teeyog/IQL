package cn.mc.report.iql.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IqlController {

    @RequestMapping("/iql")
    public String test(){
        return "iql";
    }

    @RequestMapping("/joblog")
    public String joblog(){
        return "joblog";
    }

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

}
