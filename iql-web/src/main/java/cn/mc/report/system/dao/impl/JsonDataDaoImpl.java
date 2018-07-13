package cn.mc.report.system.dao.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.mc.report.system.dao.JsonDataDao;

@Repository
public class JsonDataDaoImpl implements JsonDataDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Map> findJsonDataByType(Integer type) {
		String sql = "select id, id value, name, name label, pid from t_json_data where type="+type+" order by sort";
		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List list = query.getResultList();
		return list;
	}

	@Override
	public List<Map> findHomeMenuList() {
		String sql = "select id, name from t_json_data where type=6 and pid=-1 order by sort";
		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map> list = query.getResultList();
		return list;
	}

	@Override
	public List<Map> findSubmenuByIds(List<String> menuids) {
		String sql = "select id, name, pid from t_json_data where type=6 and pid in(:pid) order by sort";
		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setParameter("pid", menuids);
		List<Map> list = query.getResultList();
		return list;
	}

	@Override
	public List<Map> findRootmenuByIds(List<String> menuids) {
		String sql = "select id, name, pid from t_json_data where type=6 and id in(:ids) order by sort desc";
		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setParameter("ids", menuids);
		List<Map> list = query.getResultList();
		return list;
	}

}
