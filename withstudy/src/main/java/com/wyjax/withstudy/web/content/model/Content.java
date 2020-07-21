package com.wyjax.withstudy.web.content.model;

import com.wyjax.withstudy.web.common.domain.BaseTimeEntity;
import com.wyjax.withstudy.web.member.model.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Content extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id", unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private char isdelete;

    private Integer viewcount;

    @Builder
    public Content(Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.viewcount = 0;
        this.isdelete = 'N';
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void delete() {
        this.isdelete = 'Y';
    }

    public void viewCountUp() {
        this.viewcount = viewcount + 1;
    }
}