package com.wyjax.withstudy.web.study.model.repository;

import com.wyjax.withstudy.web.study.model.StudyRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyRepository extends JpaRepository<StudyRoom, Long> {

    List<StudyRoom> findByIdLessThanOrderByIdDesc(Long id, Pageable page);

    List<StudyRoom> findAllByOrderByIdDesc(Pageable page);
}