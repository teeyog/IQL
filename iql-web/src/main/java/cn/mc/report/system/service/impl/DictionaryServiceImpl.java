package cn.mc.report.system.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.mc.report.system.dao.DictionaryDao;
import cn.mc.report.system.domain.Dictionary;
import cn.mc.report.system.service.DictionaryService;
import cn.mc.report.system.vo.DictionaryVo;

@Service
@Transactional
public class DictionaryServiceImpl implements DictionaryService {

	@Autowired
	private DictionaryDao dictionaryDao;
	
	@Override
	public void save(Dictionary dictionary) {
		dictionaryDao.save(dictionary);
	}

	@Override
	public void update(Dictionary dictionary) {
		dictionaryDao.update(dictionary);
	}

	@Override
	public void remove(Integer id) {
		dictionaryDao.remove(id);
	}

	@Override
	public JSONObject findDictionaryList(DictionaryVo vo) {
		JSONObject res = new JSONObject();
		List<Dictionary> list = dictionaryDao.findDictionaryList(vo);
		int total = dictionaryDao.findCount();
		JSONArray rows = JSON.parseArray(JSON.toJSONString(list));
		res.put("total", total);
		res.put("rows", rows);
		return res;
	}

	@Override
	public Dictionary findfindDictionaryByName(DictionaryVo vo) {
		return dictionaryDao.findfindDictionaryByName(vo);
	}
}
