package com.wyjax.withstudy.web.member.portfolio.model;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PortFolioRepository extends JpaRepository<PortFolio, Long> {
    Optional<PortFolio> findByIdAndMember_Uid(Long id, String uid);
}