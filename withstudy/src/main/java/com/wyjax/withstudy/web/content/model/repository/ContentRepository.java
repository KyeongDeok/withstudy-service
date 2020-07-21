package com.wyjax.withstudy.web.content.model.repository;

import com.wyjax.withstudy.web.content.model.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {

    Page<Content> findByIsdelete(Pageable pageable, char check);

    Content findByIdAndAndIsdelete(Long id, char isdelete);
}