package com.wyjax.withstudy.web.member.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUid(String uid);

    Member findByNickname(String nickname);
}
