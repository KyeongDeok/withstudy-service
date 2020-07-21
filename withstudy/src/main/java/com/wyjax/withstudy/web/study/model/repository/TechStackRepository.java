package com.wyjax.withstudy.web.study.model.repository;


import com.wyjax.withstudy.web.study.model.TechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TechStackRepository extends JpaRepository<TechStack, Long> {
    @Query("select t.name from TechStack t where upper(t.name) LIKE CONCAT('%', upper(:tech), '%') ORDER BY t.name ASC")
    List<String> searchTech(@Param("tech") String tech);
}
