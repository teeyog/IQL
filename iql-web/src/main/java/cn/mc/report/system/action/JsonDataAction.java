package cn.mc.report.system.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.mc.report.system.service.JsonDataService;

@RestController
@RequestMapping("/sysData")
public class JsonDataAction {
	
	@Autowired
	private JsonDataService jsonDataService;
	
	@RequestMapping(value="/json/{type}", method=RequestMethod.GET)
	public JSONObject findJsonDataByType(@PathVariable("type") Integer type) {
		return jsonDataService.findJsonDataByType(type);
	}
	
	@RequestMapping(value="/menu/home", method=RequestMethod.GET)
	public JSONArray findHomeMenuList() {
		return jsonDataService.findHomeMenuList();
	}
	
}
