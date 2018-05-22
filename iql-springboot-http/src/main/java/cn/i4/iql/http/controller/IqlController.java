package cn.i4.iql.http.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class IqlController {

    @RequestMapping("/")
    public String test(){
        return "iql";
    }

    @RequestMapping("/joblog")
    public String joblog(){
        return "joblog";
    }

}
