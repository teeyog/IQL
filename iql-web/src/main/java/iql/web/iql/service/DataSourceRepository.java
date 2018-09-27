package iql.web.iql.service;

import iql.web.bean.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface DataSourceRepository extends JpaRepository<DataSource,Integer> {

    //原生SQL实现更新方法接口
    @Query(value = "update t_datasource set name=?1,pid=?2,isparent=?3,sort=?4 where id=?5", nativeQuery = true)
    @Modifying
    @Transactional
    void updateOne(String name, Integer pid, Integer isparent, Integer sort, int id);

    //原生SQL实现更新方法接口
    @Query(value = "update t_datasource set isparent=?1 where id=?2", nativeQuery = true)
    @Modifying
    @Transactional
    void updateIsParent(Integer isparent, int id);

    //原生SQL实现更新方法接口
    @Query(value = "update t_datasource set pid=?1,sort=?2 where id=?3", nativeQuery = true)
    @Modifying
    @Transactional
    void updatePid(Integer pid, Integer sort, int id);

    //原生SQL实现更新方法接口
    @Query(value = "update t_datasource set sort=sort+1 where pid=?1 and sort>=?2", nativeQuery = true)
    @Modifying
    @Transactional
    void updateDataSourceWhereSortGreatThan(Integer pid, Integer sort);

    @Query(value="select * from t_datasource a where a.pid=?1 order by sort",nativeQuery=true)
    List<DataSource> findByPid(Integer pid);

    @Query(value="select * from t_datasource a where a.path=?1",nativeQuery=true)
    List<DataSource> findByPath(String path);

    //原生SQL实现更新方法接口
    @Query(value = "update t_datasource set path=?1 where id=?2", nativeQuery = true)
    @Modifying
    @Transactional
    void updatePathById(String path, Integer id);

    @Query(value="select d.id as id,d.name as name,d.isparent as isparent from t_role r,t_role_datasource rd,t_datasource d where r.id=rd.roleid and rd.datasourceid=d.id and r.id=?1",nativeQuery=true)
    List<Object> findRoleDataSourceByRoleId(Integer id);

    @Query(value="select DISTINCT m.id,m.name,m.pid,m.path,m.sort,m.isparent " +
            "from t_user u,t_user_role ur,t_role r,t_role_datasource rm,t_datasource m " +
            "where m.id=rm.datasourceid and rm.roleid=r.id and r.id=ur.roleid and ur.userid=u.id " +
            "and m.isparent=0 and u.id=?1",nativeQuery=true)
    List<DataSource> findRoleDataSourceByUserId(Integer id);

    //利用原生的SQL进行插入操作
    @Query(value = "insert into t_role_datasource(roleid,datasourceid) value(?1,?2)", nativeQuery = true)
    @Modifying
    void addRoleDataSource(int roleid,int datasourceid);
}
