package cn.i4.report.bean;

import javax.persistence.*;

@Entity
@Table(name="job_script")
public class JobScript {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	private String name;
	
	private Integer pid;

	private String script;
	
	@Column(name="isparent")
	private Integer isParent;

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getIsParent() {
		return isParent;
	}

	public void setIsParent(Integer isParent) {
		this.isParent = isParent;
	}
	
}
