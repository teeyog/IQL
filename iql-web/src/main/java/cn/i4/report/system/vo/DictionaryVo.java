package cn.i4.report.system.vo;

import cn.i4.report.bean.BaseBean;

public class DictionaryVo extends BaseBean {

	private String name;
	
	private String type;
	
	private String useful;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUseful() {
		return useful;
	}

	public void setUseful(String useful) {
		this.useful = useful;
	}
	
}
