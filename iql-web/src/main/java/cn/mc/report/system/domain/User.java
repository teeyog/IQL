package cn.mc.report.system.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="t_user")
public class User implements UserDetails {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	private String username;
	
	private String password;
	
	private String real_name;
	
	private String phone;
	
	private Date create_time;
	
	private int status;
	
	private int type;
	
	private int isfirstlogin;
	
	@ManyToMany(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinTable(name="t_user_role", inverseJoinColumns=@JoinColumn(name="roleid"), joinColumns=@JoinColumn(name="userid"))
	private Set<Role> roles = new HashSet<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getReal_name() {
		return real_name;
	}

	public void setRead_name(String real_name) {
		this.real_name = real_name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIsfirstlogin() {
		return isfirstlogin;
	}

	public void setIsfirstlogin(int isfirstlogin) {
		this.isfirstlogin = isfirstlogin;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> auths = new HashSet<>();
        Set<Role> roles = this.getRoles();
        for (Role role : roles) {
            auths.add(new SimpleGrantedAuthority(role.getRolename()));
        }
        return auths;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
