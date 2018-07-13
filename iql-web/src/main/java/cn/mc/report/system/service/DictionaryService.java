package cn.mc.report.system.service;

import com.alibaba.fastjson.JSONObject;

import cn.mc.report.system.domain.Dictionary;
import cn.mc.report.system.vo.DictionaryVo;

public interface DictionaryService {

	public void save(Dictionary dictionary);
	
	public void update(Dictionary dictionary);
	
	public void remove(Integer id);
	
	public JSONObject findDictionaryList(DictionaryVo vo);

	public Dictionary findfindDictionaryByName(DictionaryVo vo);
	
}
