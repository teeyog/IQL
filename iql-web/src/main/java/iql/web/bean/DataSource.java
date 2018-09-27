package iql.web.bean;

import javax.persistence.*;

@Entity
@Table(name="t_datasource")
public class DataSource {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	private String name;
	
	private Integer pid;
	
	@Column(name="isparent")
	private Integer isParent;

	private Integer sort;

	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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
