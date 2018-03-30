package cn.i4.iql.http.service.primary;

import cn.i4.iql.http.bean.primary.IqlExcution;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

public interface IqlExcutionRepository  extends JpaRepository<IqlExcution, Long> {

}
