package com.wyjax.withstudy.web.content.dto;

import com.wyjax.withstudy.web.common.domain.TimeConvert;
import com.wyjax.withstudy.web.content.model.Content;
import com.wyjax.withstudy.web.member.dto.MemberRequestDto;
import lombok.Getter;

@Getter
public class ContentResponseDto extends TimeConvert {

    private Long id;
    private MemberRequestDto member;
    private String title;
    private String content;
    private Integer viewCount;
    private String createDate;

    public ContentResponseDto(Content c) {
        this.id = c.getId();
        this.title = c.getTitle();
        this.content = c.getContent();
        this.viewCount = c.getViewcount();
        this.member = MemberRequestDto.builder()
                .uid(c.getMember().getUid())
                .nickname(c.getMember().getNickname())
                .build();
        this.createDate = timeConvert(c.getCreateDate());
    }
}
