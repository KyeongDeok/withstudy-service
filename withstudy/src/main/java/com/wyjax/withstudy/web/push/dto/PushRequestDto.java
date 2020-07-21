package com.wyjax.withstudy.web.push.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PushRequestDto {
    private String message;
    private String url;
    private String toId;
    private String fromId;
}
