package cn.i4.report.system.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import cn.i4.report.system.dao.MenuDao;
import cn.i4.report.system.domain.Menu;
import cn.i4.report.system.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public class MenuDaoImpl implements MenuDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Menu> findAllMenuList() {
		String sql = "select id, name, pid, url, icon, reorder from t_menu_copy order by reorder";
		Query query = em.createNativeQuery(sql, Menu.class);
		return query.getResultList();
	}

	@Override
	public Integer findCount() {
		String sql = "select FOUND_ROWS()";
		Query query = em.createNativeQuery(sql);
		Integer total = ((Number)query.getSingleResult()).intValue();
		return total;
	}

	@Override
	public Integer addMenu(Menu menu) {
		String sql="insert into t_menu_copy(name, pid,url,icon,reorder) values(?, ?, ?, ?, ?)";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, menu.getName());
		query.setParameter(2, menu.getPid());
		query.setParameter(3, menu.getUrl());
		query.setParameter(4, menu.getIcon());
		query.setParameter(5, menu.getReorder());
		Integer result = query.executeUpdate();
		return result;
	}

	@Override
	public Integer updateMenu(Menu menu) {
		String sql="update t_menu_copy set name=?,pid=?,url=?,icon=?, reorder=? where id=?";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, menu.getName());
		query.setParameter(2, menu.getPid());
		query.setParameter(3, menu.getUrl());
		query.setParameter(4, menu.getIcon());
		query.setParameter(5, menu.getReorder());
		query.setParameter(6, menu.getId());
		Integer result = query.executeUpdate();
		return result;
	}

	@Override
	public Integer delMenu(Menu menu) {
		String sql="delete from t_menu_copy where id=?";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, menu.getId());
		Integer result = query.executeUpdate();
		return result;
	}

	@Override
	public List<Menu> findMenuListByUid(User user) {
		String sql = "select DISTINCT m.id,m.name,m.pid,m.url,m.reorder,m.icon " +
				"from t_user u,t_user_role ur,t_role r,t_role_menu rm,t_menu_copy m " +
				"where m.id=rm.menuid and rm.roleid=r.id and r.id=ur.roleid and ur.userid=u.id" +
				" and u.id=:uid ORDER BY reorder";
		Query query = em.createNativeQuery(sql, Menu.class);
		query.setParameter("uid", user.getId());
		return query.getResultList();
	}

	@Override
	public Menu findMenuById(Menu menu) {
		String sql = "SELECT  id,name,pid,url,icon,reorder from t_menu_copy where id=:id ORDER BY reorder ";
		Query query = em.createNativeQuery(sql, Menu.class);
		query.setParameter("id", menu.getId());
		return query.getResultList().size()==0 ? null : (Menu) query.getResultList().get(0);
	}

	@Override
	public List<Menu> findMenuByPid(Menu menu) {
		String sql = "SELECT  id,name,pid,url,icon,reorder from t_menu_copy where pid=:pid ORDER BY reorder";
		Query query = em.createNativeQuery(sql, Menu.class);
		query.setParameter("pid", menu.getPid());
		return query.getResultList();
	}

}
