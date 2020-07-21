package com.wyjax.withstudy.web.study.model;

import com.wyjax.withstudy.web.common.domain.BaseTimeEntity;
import com.wyjax.withstudy.web.member.model.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Table(name = "studyroom")
@Entity
public class StudyRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studyroom_id")
    private Long id;

    @Column(nullable = false)
    private String roomName;

    @Column(columnDefinition = "TEXT")
    private String roomDesc;

    @Column
    private int roomLimit;

    @Column
    private int joinNum;

    @Column(length = 2048)
    private String techList;

    @Enumerated(EnumType.STRING)
    private StudyStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "studyRoom", cascade = CascadeType.ALL)
    private List<StudyMember> studyMembers = new ArrayList<>();

    @OneToMany(mappedBy = "studyRoom", cascade = CascadeType.ALL)
    private List<StudyRequest> studyRequests = new ArrayList<>();

    @Builder
    public StudyRoom(String roomName, String roomDesc,
                     int roomLimit, Member member,
                     int joinNumm, StudyStatus status,
                     String techs) {
        this.roomName = roomName;
        this.roomDesc = roomDesc;
        this.roomLimit = roomLimit;
        this.member = member;
        this.joinNum = joinNumm;
        this.status = status;
        this.techList = techs;
    }

    public void memberJoin(StudyMember studyMember) {
        this.joinNum = joinNum + 1;
        this.getStudyMembers().add(studyMember);
    }

    public void memberCancel(int idx) {
        this.joinNum = joinNum - 1;
        this.getStudyMembers().remove(idx);
    }

    public void updateStatus(StudyStatus status) {
        this.status = status;
    }
}
