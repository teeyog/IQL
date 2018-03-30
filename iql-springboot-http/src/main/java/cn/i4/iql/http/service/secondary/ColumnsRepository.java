package cn.i4.iql.http.service.secondary;

import cn.i4.iql.http.bean.secondary.COLUMNS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColumnsRepository extends JpaRepository<COLUMNS, Long> {

    List<COLUMNS> findCOLUMNSByCdId(Long cdId);

}
