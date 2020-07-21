package com.wyjax.withstudy.web.content.dto;

import lombok.*;

@Getter
@NoArgsConstructor
public class ContentRequestDto {
    private String title;
    private String content;

    public ContentRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}