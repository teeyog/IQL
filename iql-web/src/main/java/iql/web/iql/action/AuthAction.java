package iql.web.iql.action;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthAction {

    @RequestMapping(value = "/checkAuth")
    public String checkAuth(String data) {
        System.out.println(data);
        return "pass";
    }

}
