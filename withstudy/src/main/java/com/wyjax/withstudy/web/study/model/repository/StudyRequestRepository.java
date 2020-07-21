package com.wyjax.withstudy.web.study.model.repository;


import com.wyjax.withstudy.web.study.model.StudyRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyRequestRepository extends JpaRepository<StudyRequest, Long> {
    StudyRequest findByMemberIdAndStudyRoomId(Long member_id, Long studyroom_id);

    Optional<StudyRequest> findByMemberUidAndStudyRoomId(String uid, Long studyroom_id);

    List<StudyRequest> findByStudyRoomId(Long id);
}
