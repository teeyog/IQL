package cn.i4.report.system.action;

import cn.i4.report.bean.BaseBean;
import cn.i4.report.system.domain.Menu;
import cn.i4.report.system.service.MenuService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by admin on 2017/7/13.
 */

@RestController
@RequestMapping("/menu")
public class MenuAction {

    @Autowired
    private MenuService menuService;

    @RequestMapping(value="/system/select", method= RequestMethod.GET)
    public JSONObject findMenuSelect(){
        return menuService.findMenuSelect();
    }

    @RequestMapping(value="/system/parentlist", method= RequestMethod.GET)
    public JSONObject findAllMenu(BaseBean base){
        return menuService.findParentMenu();
    }

    @RequestMapping(value="/system/childlist", method= RequestMethod.GET)
    public JSONObject findChildMenu(Menu menu){
        return menuService.findChildMenu(menu);
    }

    @RequestMapping(value="/system/add", method= RequestMethod.POST)
    public Integer addMenu(Menu menu){
        return menuService.addMenu(menu);
    }

    @RequestMapping(value="/system/update", method= RequestMethod.POST)
    public Integer updateMenu(Menu menu){
        return menuService.updateMenu(menu);
    }

    @RequestMapping(value="/system/delete", method= RequestMethod.GET)
    public Integer delMenu(Menu menu){
        return menuService.delMenu(menu);
    }
}
