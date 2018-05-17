package cn.i4.iql.http.service;

import cn.i4.iql.http.bean.IqlExcution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IqlExcutionRepository  extends JpaRepository<IqlExcution, Long> {

}
