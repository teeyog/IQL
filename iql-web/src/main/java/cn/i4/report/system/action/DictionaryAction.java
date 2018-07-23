package cn.i4.report.system.action;

import cn.i4.report.system.domain.Dictionary;
import cn.i4.report.system.service.DictionaryService;
import cn.i4.report.system.vo.DictionaryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/dictionary")
public class DictionaryAction {

	@Autowired
	private DictionaryService dictionaryService;
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public void save(Dictionary dictionary) {
		dictionaryService.save(dictionary);
	}
	
	@RequestMapping(value="/update", method=RequestMethod.PUT)
	public void update(Dictionary dictionary) {
		dictionaryService.update(dictionary);
	}
	
	@RequestMapping(value="/remove/{id}", method=RequestMethod.DELETE)
	public void remove(@PathVariable("id") Integer id) {
		dictionaryService.remove(id);
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public JSONObject findDictionaryList(DictionaryVo vo) {
		return dictionaryService.findDictionaryList(vo);
	}
	
}
