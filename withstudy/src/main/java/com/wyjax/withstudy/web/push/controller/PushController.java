package com.wyjax.withstudy.web.push.controller;

import com.wyjax.withstudy.config.auth.dto.SessionUser;
import com.wyjax.withstudy.web.push.service.PushService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

import static com.wyjax.withstudy.web.member.controller.MemberController.SESSION_USER_ID;

@RequiredArgsConstructor
@Controller
public class PushController {
    private final HttpSession session;
    private final PushService pushService;

    /**
     * 알림 확인
     */
    @GetMapping("/push/{id}")
    public String pushEvent(@PathVariable("id") Long id) {
        SessionUser user = (SessionUser) session.getAttribute(SESSION_USER_ID);

        if (user == null) {
            return null;
        }

        return "redirect:/" + pushService.getPushContent(session, id, user.getUid());
    }
}
