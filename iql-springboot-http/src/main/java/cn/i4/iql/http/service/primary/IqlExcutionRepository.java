package cn.i4.iql.http.service.primary;

import cn.i4.iql.http.bean.primary.IqlExcution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IqlExcutionRepository  extends JpaRepository<IqlExcution, Long> {

}
