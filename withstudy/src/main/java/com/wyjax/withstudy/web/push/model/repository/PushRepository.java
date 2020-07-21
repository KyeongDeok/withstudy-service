package com.wyjax.withstudy.web.push.model.repository;

import com.wyjax.withstudy.web.push.model.Push;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PushRepository extends JpaRepository<Push, Long> {

    @Query("select count (p.id) from Push p where p.toid = :uid and p.isRead = false")
    Optional<Integer> getCountpush(@Param("uid") String uid);

    List<Push> findByToidAndIdLessThanOrderByIdDesc(String toid, Long id, Pageable page);
}
