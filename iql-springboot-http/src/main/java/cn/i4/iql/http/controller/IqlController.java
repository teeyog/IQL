package cn.i4.iql.http.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class IqlController {

    @RequestMapping("/iql")
    public String test(){
        return "iql";
    }

}
