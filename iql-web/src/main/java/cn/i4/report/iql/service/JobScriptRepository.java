package cn.i4.report.iql.service;

import cn.i4.report.bean.JobScript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

public interface JobScriptRepository  extends JpaRepository<JobScript,Integer> {

    //原生SQL实现更新方法接口
    @Query(value = "update job_script set name=?1,pid=?2,isparent=?3 where id=?4", nativeQuery = true)
    @Modifying
    @Transactional
    void updateOne(String name, Integer pid, Integer isparent, int id);

    //原生SQL实现更新方法接口
    @Query(value = "update job_script set isparent=?1 where id=?2", nativeQuery = true)
    @Modifying
    @Transactional
    void updateIsParent(Integer isparent, int id);

    //原生SQL实现更新方法接口
    @Query(value = "update job_script set pid=?1 where id=?2", nativeQuery = true)
    @Modifying
    @Transactional
    void updatePid(Integer pid, int id);

    @Query(value="select * from job_script a where a.pid=?1",nativeQuery=true)
    List<JobScript> findByPid(Integer pid);
}
