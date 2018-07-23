package cn.i4.report.system.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import cn.i4.report.system.dao.DictionaryDao;
import cn.i4.report.system.domain.Dictionary;
import cn.i4.report.system.vo.DictionaryVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class DictionaryDaoImpl implements DictionaryDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void save(Dictionary dictionary) {
		em.persist(dictionary);
	}

	@Override
	public void update(Dictionary dictionary) {
		Dictionary query = em.find(Dictionary.class, dictionary.getId());
		if(query != null) {
			query.setValue(dictionary.getValue());
			query.setName(dictionary.getName());
			query.setDescription(dictionary.getDescription());
			query.setType(dictionary.getType());
			query.setUseful(dictionary.getUseful());
			query.setSortnum(dictionary.getSortnum());
		} 
	}

	@Override
	public void remove(Integer id) {
		Dictionary query = em.find(Dictionary.class, id);
		if(query != null) 
			em.remove(query);
	}
	
	@Override
	public Integer findCount() {
		String sql = "select FOUND_ROWS()";
		Query query = em.createNativeQuery(sql);
		Integer total = ((Number)query.getSingleResult()).intValue();
		return total;
	}

	@Override
	public List<Dictionary> findDictionaryList(DictionaryVo vo) {
		String con = "";
		if(StringUtils.isNotBlank(vo.getName())) {
			con += "and name='"+vo.getName()+"' ";
		}
		if(StringUtils.isNotBlank(vo.getType())) {
			con += "and type="+vo.getType()+" ";
		}
		if(StringUtils.isNotBlank(vo.getUseful())) {
			con += "and useful="+vo.getUseful()+" ";
		}
		String sql = "select sql_calc_found_rows id, name, value, description, type, useful, sortnum from t_dictionary where 1=1 "+con+"order by sortnum limit :offset, :limit";
		Query query = em.createNativeQuery(sql, Dictionary.class);
		query.setParameter("offset", vo.getOffset());
		query.setParameter("limit", vo.getLimit());
		return query.getResultList();
	}

	@Override
	public Dictionary findfindDictionaryByName(DictionaryVo vo) {
		String sql = "select id, name, value, description, type, useful, sortnum from t_dictionary where useful=2 and name=:name";
		Query query = em.createNativeQuery(sql, Dictionary.class);
		query.setParameter("name", vo.getName());
		List<Dictionary> list = query.getResultList();
		return list.size()==0 ? null : list.get(0);
	}
}
