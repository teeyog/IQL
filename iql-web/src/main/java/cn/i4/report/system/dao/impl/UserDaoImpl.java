package cn.i4.report.system.dao.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import cn.i4.report.bean.BaseBean;
import cn.i4.report.system.dao.UserDao;
import cn.i4.report.system.domain.EntityUser;
import cn.i4.report.system.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Integer findCount() {
		String sql = "select FOUND_ROWS()";
		Query query = em.createNativeQuery(sql);
		Integer total = ((Number)query.getSingleResult()).intValue();
		return total;
	}

	@Override
	public User findByUsername(String username) {
		String sql="select id, username, `password`, real_name, phone, create_time, status, type, isfirstlogin from t_user where username=:username";
		Query query = em.createNativeQuery(sql, User.class);
		query.setParameter("username", username);
		List<User> list = query.getResultList();
		return list!=null && list.size()>0? list.get(0):null;
	}


	@Override
	public List<Map> findUserList(BaseBean base){
		String sql = "select id,username,realName,isfirstlogin,createTime,status,phone,group_concat(roleid) roleid,group_concat(rolename) rolename,group_concat(descs) descs from (" +
				"select u.id,u.username,u.real_name realName,u.isfirstlogin,date_format(u.create_time,'%Y-%m-%d %H.%i.%s') createTime,u.status,u.phone,r.id roleid,r.rolename,r.descs " +
				"from t_user u LEFT JOIN t_user_role ur on ur.userid=u.id LEFT JOIN t_role r on r.id=ur.roleid where (u.username like :search or u.real_name like :search )) t " +
				"group by id,username,realName,isfirstlogin,createTime,status,phone";
		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		if(base.getSearch()==null){
			base.setSearch("");
		}
		query.setParameter("search", "%"+base.getSearch().trim()+"%");
		//query.setParameter("offset", base.getOffset());
		//query.setParameter("limit", base.getLimit());
		List<Map> list = query.getResultList();
		return list;
	}

	@Override
	public EntityUser findUserById(EntityUser user) {
		String sql="select id, username, `password`, real_name, phone, create_time, status, type, isfirstlogin from t_user where id=:id";
		Query query = em.createNativeQuery(sql, EntityUser.class);
		query.setParameter("id", user.getId());
		List<EntityUser> list = query.getResultList();
		return list!=null && list.size()>0? list.get(0):null;
	}

	@Override
	public Integer addUser(EntityUser user) {
		//需要获取主键
		em.persist(user);
		em.flush();
		return 1;

//		String sql="insert into t_user(username, password,real_name,phone,create_time, status, 'type',isfirstlogin) values(?, ?, ?, ?, ?, ?, ?, ?)";
//		Query query = em.createNativeQuery(sql);
//		query.setParameter(1, user.getUsername());
//		query.setParameter(2, MD5Util.getMD5String(user.getPassword()));
//		query.setParameter(3, user.getReal_name());
//		query.setParameter(4, user.getPhone());
//		query.setParameter(5, user.getCreate_time());
//		query.setParameter(6, user.getStatus());
//		query.setParameter(7, user.getType());
//		query.setParameter(8, user.getIsfirstlogin());
//		Integer result = query.executeUpdate();
//		return result;
	}

	@Override
	public Integer delUser(EntityUser user) {
		String sql="delete from t_user where id=?";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, user.getId());
		Integer result = query.executeUpdate();
		return result;
	}

	@Override
	public Integer updateUser(EntityUser user) {
		Integer result = 0;
		if("@#$1-2-3-i4-5-6-7$#@".equals(user.getPassword().trim())){
			String sql="update t_user set username=?,real_name=?,isfirstlogin=?, create_time=? where id=?";
			Query query = em.createNativeQuery(sql);
			query.setParameter(1, user.getUsername());
			query.setParameter(2, user.getReal_name());
			query.setParameter(3, user.getIsfirstlogin());
			query.setParameter(4, user.getCreate_time());
			query.setParameter(5, user.getId());
			result = query.executeUpdate();
		}else{
			String sql="update t_user set username=?,password=?,real_name=?,isfirstlogin=?, create_time=? where id=?";
			Query query = em.createNativeQuery(sql);
			query.setParameter(1, user.getUsername());
			query.setParameter(2, user.getPassword());
			query.setParameter(3, user.getReal_name());
			query.setParameter(4, user.getIsfirstlogin());
			query.setParameter(5, user.getCreate_time());
			query.setParameter(6, user.getId());
			result = query.executeUpdate();
		}

		return result;
	}

	@Override
	public List<Map> findUserRole(EntityUser user) {
		String sql = "select r.id,r.rolename,r.descs from t_user u,t_user_role ur,t_role r where r.id=ur.roleid and ur.userid=u.id and u.id=:id";
		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		query.setParameter("id", user.getId());
		List<Map> list = query.getResultList();
		return list;
	}

	@Override
	public Integer addUserRole(EntityUser user, String roles) {

		if(roles==null){
			return 0;
		}
		String[] role = roles.split(",");
		String sqls = "";
		for(String rid:role){
			if(rid!=null&&!rid.equals("")){
				sqls+="("+user.getId()+","+rid+")"+",";
			}
		}
		sqls = StringUtils.strip(sqls,",");
		String sql = "insert into t_user_role (userid,roleid) values "+sqls;
		Query query = em.createNativeQuery(sql);
		Integer result = query.executeUpdate();
		return result;
	}

	@Override
	public Integer delUserRole(EntityUser user) {
		String sql="delete from t_user_role where userid=?";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, user.getId());
		Integer result = query.executeUpdate();
		return result;
	}

	@Override
	public List<Map> findAllUser(EntityUser user) {
		String sql = "select id,username,real_name from t_user";
		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map> list = query.getResultList();
		return list;
	}
}
