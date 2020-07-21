package com.wyjax.withstudy.config.auth.dto;

import com.wyjax.withstudy.web.member.model.Member;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String uid;
    private String name;
    private String picture;

    public SessionUser(Member member) {
        this.uid = member.getUid();
        this.name = member.getNickname();
        this.picture = member.getPicture();
    }

    public void changeName(String name) {
        this.name = name;
    }
}