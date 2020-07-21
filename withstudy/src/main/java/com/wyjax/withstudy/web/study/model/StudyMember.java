package com.wyjax.withstudy.web.study.model;

import com.wyjax.withstudy.web.common.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@Getter
@Table(name = "studymember")
@Entity
public class StudyMember extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "studymember_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studyroom_id")
    private StudyRoom studyRoom;

    private String memberId;

    @Builder
    public StudyMember(StudyRoom studyRoom,
                       String memberId) {
        this.studyRoom = studyRoom;
        this.memberId = memberId;
    }
}