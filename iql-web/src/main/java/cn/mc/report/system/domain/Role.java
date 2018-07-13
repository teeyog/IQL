package cn.mc.report.system.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="t_role")
public class Role {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	private String rolename;
	
	private String descs;
	
//	@ManyToMany(cascade=CascadeType.REFRESH, mappedBy="roles")
//	private Set<User> users;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRolename() {
		return rolename;
	}
	
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

//	public Set<User> getUsers() {
//		return users;
//	}
//
//	public void setUsers(Set<User> users) {
//		this.users = users;
//	}

}
