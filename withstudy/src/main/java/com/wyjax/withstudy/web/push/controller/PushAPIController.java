package com.wyjax.withstudy.web.push.controller;

import com.wyjax.withstudy.config.auth.dto.SessionUser;
import com.wyjax.withstudy.web.push.dto.PushResponseDto;
import com.wyjax.withstudy.web.push.service.PushService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.wyjax.withstudy.web.member.controller.MemberController.SESSION_USER_ID;

@RequiredArgsConstructor
@RestController
public class PushAPIController {
    private final PushService pushService;
    private final HttpSession session;

    /**
     * SSE Connection
     */
    @GetMapping(value = "/push", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect() throws InterruptedException {
        SessionUser user = (SessionUser) session.getAttribute(SESSION_USER_ID);

        if (user == null) {
            return null;
        }

        return pushService.connectUser(user.getUid());
    }

    /**
     * push update (SSE)
     */
    @PutMapping("/push")
    public int pushUpdate() {
        SessionUser user = (SessionUser) session.getAttribute(SESSION_USER_ID);
        int pushCount = 0;

        if (user != null) {
            pushCount = pushService.getPushCount(user.getUid());
            session.setAttribute("pushCount", pushCount);
        }

        return pushCount;
    }

    /**
     * 알림 call
     */
    @GetMapping("/pushList")
    public List<PushResponseDto> pushList(int size, Long lastId) {
        SessionUser user = (SessionUser) session.getAttribute(SESSION_USER_ID);

        if (lastId == 0 || user == null) {
            return null;
        }

        return pushService.getPushList(user.getUid(), PageRequest.of(0, size), lastId);
    }
}
