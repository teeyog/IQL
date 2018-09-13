package iql.web.system.action;

import iql.web.bean.BaseBean;
import iql.web.system.domain.Role;
import iql.web.system.service.RoleService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by admin on 2017/7/14.
 */

@RestController
@RequestMapping("/role")
public class RoleAction {

    @Autowired
    private RoleService roleService;

    @RequestMapping(value="/system/list", method= RequestMethod.GET)
    public JSONObject findAllRole(BaseBean base){
        return roleService.findAllRole(base);
    }

    @RequestMapping(value="/system/rolemenu", method= RequestMethod.GET)
    public JSONArray findRoleMenu(Role role){
        return roleService.findRoleMenu(role);
    }

    @RequestMapping(value="/system/add", method= RequestMethod.POST)
    public Integer addRole(Role role,String menus){
        return roleService.addRole(role,menus);
    }
    @RequestMapping(value="/system/update", method= RequestMethod.POST)
    public Integer updateRole(Role role,String menus){
        return roleService.updateRole(role,menus);
    }
    @RequestMapping(value="/system/delete", method= RequestMethod.GET)
    public Integer delRole(Role role){
        return roleService.delRole(role);
    }

    @RequestMapping(value="/system/select", method= RequestMethod.GET)
    public JSONObject findRoleSelect(){
        return roleService.findRoleSelect();
    }
}
