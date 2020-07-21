package com.wyjax.withstudy.web.study.model.repository;

import com.wyjax.withstudy.web.study.model.StudyMember;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {

    List<StudyMember> findAllByMemberIdOrderByIdDesc(String uid, Pageable page);

    List<StudyMember> findByIdLessThanAndMemberIdOrderById(Long id, String uid, Pageable page);
}
