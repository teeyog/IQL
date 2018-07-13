package cn.mc.report.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.mc.report.bean.BaseBean;
import cn.mc.report.system.dao.MenuDao;
import cn.mc.report.system.domain.Menu;
import cn.mc.report.system.domain.Role;
import cn.mc.report.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.mc.report.system.dao.RoleDao;
import cn.mc.report.system.service.RoleService;
import cn.mc.report.util.TreeBuilder;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;

	@Autowired
    private MenuDao menuDao;
	
	@Override
	public JSONObject findRoleMenuByIds(List<Integer> ids) {
		JSONObject res = new JSONObject(true);
		List<Map> list = roleDao.findRoleMenuByIds(ids);
		JSONArray rows = JSON.parseArray(JSON.toJSONString(list));
		
		JSONObject node = new JSONObject();
		JSONArray tree = new TreeBuilder(rows).buildTree();
		res.put("rows", tree);
		return res;
	}

	@Override
	public JSONObject findAllRole(BaseBean base) {
		JSONObject res = new JSONObject(true);
		List<Map> list = roleDao.findAllRole();
		int total = list.size();
        list = DataUtil.pageFormate(list,base.getOffset(),base.getLimit());
		JSONArray rows = JSON.parseArray(JSON.toJSONString(list));
		res.put("rows", rows);
		res.put("total", total);
		return res;
	}

    @Override
    public JSONArray findRoleMenu(Role role) {
        List<Map> roleMenus = roleDao.findRoleMenuByRoleId(role);
        List<Integer> roleIds = new ArrayList<Integer>();
        List<Integer> rolePids = new ArrayList<Integer>();
        for(Map m:roleMenus){
            roleIds.add((Integer) m.get("id"));
            rolePids.add((Integer) m.get("pid"));
        }
        List<Menu> menuList = menuDao.findAllMenuList();
        List<Menu> parMenu = new ArrayList<Menu>();
        List<Menu> chiMenu = new ArrayList<Menu>();
        JSONArray resArray = new JSONArray();
        JSONObject all = new JSONObject();
        for(Menu menu:menuList){
            if(menu.getPid()==-1){
                parMenu.add(menu);
            }else{
                chiMenu.add(menu);
            }
        }

        JSONArray parArray = new JSONArray();
        for(Menu menu:parMenu){
            JSONObject parent = new JSONObject();
            JSONArray chiArray = new JSONArray();
            parent.put("id", menu.getId());
            parent.put("text", "<span style='font-weight:bold;color:#5D934B'>"+menu.getName()+"</span>");
            //parent.put("text", menu.getName());
            if(!(rolePids.contains(menu.getId()))&&roleIds.contains(menu.getId())){
                parent.put("checked", true);
            }
            if("IQL".equals(menu.getName())){
                parent.put("checked", true);
            }
            parent.put("iconCls", "icon-filter");
            for(Menu cm:chiMenu){
                if(menu.getId().equals(cm.getPid())){
                    parent.put("state","open");
                    JSONObject child = new JSONObject();
                    child.put("id", cm.getId());
                    child.put("text", cm.getName());
                    child.put("iconCls", "icon-filter");
                    if(roleIds.contains(cm.getId())){
                        child.put("checked", true);
                    }
                    if("修改密码".equals(cm.getName())){
                        child.put("checked", true);
                    }

                    chiArray.add(child);
                }
            }
            parent.put("children", chiArray);
            parArray.add(parent);
        }
        all.put("text", "<span style='font-weight:bold;color:#3F3F3F';font-size: 120%>全选</span>");
        //all.put("text", "全选");
        all.put("iconCls", "icon-filter");
        all.put("state","open");
        all.put("children", parArray);
        resArray.add(all);
        return resArray;
    }

    @Override
    public Integer addRole(Role role,String menus) {
        roleDao.addRole(role);
        roleDao.addRoleMenu(role,menus);
        return 1;
    }

    @Override
    public Integer updateRole(Role role,String menus) {
        roleDao.updateRole(role);
        roleDao.delRoleMenu(role);
        roleDao.addRoleMenu(role,menus);
        return 1;
    }

    @Override
    public Integer delRole(Role role) {
        roleDao.delRoleMenu(role);
        return roleDao.delRole(role);
    }

    @Override
    public JSONObject findRoleSelect() {
        JSONObject res = new JSONObject();
        JSONArray options = new JSONArray();
        List<Map> list = roleDao.findAllRole();
        JSONArray rows = JSON.parseArray(JSON.toJSONString(list));
        JSONObject first = new JSONObject();
//        first.put("value", "-1");
//        first.put("label", "未赋予角色");
//        options.add(first);
        for(int i=0; i<rows.size(); i++) {
            JSONObject row = rows.getJSONObject(i);
            JSONObject o = new JSONObject();
            o.put("value", row.getString("id"));
            o.put("label", row.getString("rolename"));
            options.add(o);
        }
        res.put("options", options);
        return res;
    }
}
