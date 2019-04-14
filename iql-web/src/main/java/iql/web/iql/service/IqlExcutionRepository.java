package iql.web.iql.service;

import iql.web.bean.IqlExcution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IqlExcutionRepository  extends JpaRepository<IqlExcution, Long> {

    @Query(value="select * from iql_excution a where user=:user and a.iql like CONCAT('%',:search,'%')",nativeQuery=true)
    List<IqlExcution> findByUserAndIqlLike(@Param("user") String user, @Param("search") String search);

}
