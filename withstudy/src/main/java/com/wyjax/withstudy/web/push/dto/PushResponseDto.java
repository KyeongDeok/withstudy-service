package com.wyjax.withstudy.web.push.dto;

import com.wyjax.withstudy.web.common.domain.TimeConvert;
import com.wyjax.withstudy.web.push.model.Push;
import lombok.Getter;

@Getter
public class PushResponseDto extends TimeConvert {
    Long id;
    String message;
    String url;
    String toId;
    String fromId;
    String occurTime;
    boolean isRead;

    public PushResponseDto(Push p) {
        this.id = p.getId();
        this.message = p.getMessage();
        this.url = p.getUrl();
        this.toId = p.getToid();
        this.fromId = p.getToid();
        this.isRead = p.isRead();
        this.occurTime = timeConvert(p.getCreateDate());
    }
}
