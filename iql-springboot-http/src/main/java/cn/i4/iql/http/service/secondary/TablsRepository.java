package cn.i4.iql.http.service.secondary;

import cn.i4.iql.http.bean.secondary.DBS;
import cn.i4.iql.http.bean.secondary.TBLS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TablsRepository extends JpaRepository<TBLS, Long> {

    List<TBLS> findTBLSByDbId(Long dbId);

    TBLS findTBLSByTblName(String tableName);

}
