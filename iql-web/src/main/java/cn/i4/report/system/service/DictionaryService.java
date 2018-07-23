package cn.i4.report.system.service;

import cn.i4.report.system.domain.Dictionary;
import com.alibaba.fastjson.JSONObject;

import cn.i4.report.system.vo.DictionaryVo;

public interface DictionaryService {

	public void save(Dictionary dictionary);
	
	public void update(Dictionary dictionary);
	
	public void remove(Integer id);
	
	public JSONObject findDictionaryList(DictionaryVo vo);

	public Dictionary findfindDictionaryByName(DictionaryVo vo);
	
}
