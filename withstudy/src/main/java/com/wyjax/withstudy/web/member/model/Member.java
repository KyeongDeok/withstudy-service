package com.wyjax.withstudy.web.member.model;

import com.wyjax.withstudy.web.common.domain.BaseTimeEntity;
import com.wyjax.withstudy.web.content.model.Content;
import com.wyjax.withstudy.web.member.portfolio.model.PortFolio;
import com.wyjax.withstudy.web.study.model.StudyRequest;
import com.wyjax.withstudy.web.study.model.StudyRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String uid;

    @Column(length = 30)
    private String nickname;

    @Column
    private String serverName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private String picture;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Content> contents = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<StudyRoom> studyRooms = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<PortFolio> portFolios = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<StudyRequest> requests = new ArrayList<>();

    @Builder
    public Member(String uid,
                  String nickname,
                  Role role,
                  String picture,
                  String serverName) {
        this.uid = uid;
        this.nickname = nickname;
        this.role = role;
        this.picture = picture;
        this.serverName = serverName;
    }

    public void nickCahnge(String nickname) {
        this.nickname = nickname;
    }

    public String getRole() {
        return this.role.getKey();
    }

    public Member update(String picture) {
        this.picture = picture;
        return this;
    }
}