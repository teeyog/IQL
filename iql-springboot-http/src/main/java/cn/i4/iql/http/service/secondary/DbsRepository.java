package cn.i4.iql.http.service.secondary;

import cn.i4.iql.http.bean.secondary.DBS;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DbsRepository extends JpaRepository<DBS, Long> {

    DBS findDBSByName(String name);

}
