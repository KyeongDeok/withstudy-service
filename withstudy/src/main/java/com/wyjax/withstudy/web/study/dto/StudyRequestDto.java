package com.wyjax.withstudy.web.study.dto;

import com.wyjax.withstudy.web.member.model.Member;
import lombok.Getter;

@Getter
public class StudyRequestDto {
    private String name;
    private String uid;
    private String picture;

    public StudyRequestDto(Member member) {
        this.name = member.getNickname();
        this.uid = member.getUid();
        this.picture = member.getPicture();
    }
}
