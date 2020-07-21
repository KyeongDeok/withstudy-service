package com.wyjax.withstudy.web.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequestDto {
    String uid;
    String nickname;

    @Builder
    public MemberRequestDto(String uid, String nickname) {
        this.uid = uid;
        this.nickname = nickname;
    }
}
