package com.wyjax.withstudy.web.push.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Getter
public class UserChannel {
    private String userId;
    private SseEmitter emitter;

    public UserChannel(String userId, SseEmitter emitter) {
        this.userId = userId;
        this.emitter = emitter;
    }

    public void send(String message) {
        log.info("푸시 전송 > userId : " + userId + ", message : "  + message);
        try {
            this.emitter.send(message, MediaType.APPLICATION_JSON_UTF8);
        }
        catch (IOException e) {
            e.getMessage();
        }
    }
}
